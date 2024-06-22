import json
import logging

from peewee import IntegrityError, DataError

from database_connection.database import db
from model.role import Role
from model.user import User
from model.user_roles import UserRoles
from service_stub import service_pb2_grpc
from service_stub.service_pb2 import Result, Token, UserId, UserBody, UserPatch, UserData
from utils import get_token, decode_token, add_to_black_list, black_list, Status


class IdmService(service_pb2_grpc.IdmServiceServicer):
    def CreateUser(self, request, context):
        request: UserBody
        logging.info(f'Received request for create user... {request}')

        if request.role == 'physician':
            if request.token is None:
                return Result(status=Status.FORBIDDEN, message='Forbidden')
            if request.token in black_list:
                return Result(status=Status.FORBIDDEN, message='Token blacklisted')
            status, message = decode_token(request.token)
            if status is not None:
                return Result(status=status, message=message)
            if 'admin' not in message['roles']:
                return Result(status=Status.FORBIDDEN, message='Forbidden')
        try:
            role_id: Role = Role.get(Role.name == request.role)
        except Role.DoesNotExist:
            return Result(status=Status.UNPROCESSABLE_ENTITY, message='Invalid role')

        try:
            user = User.create(username=request.username, password=request.password)
            UserRoles.create(user_id=user.id, role_id=role_id.id)
            logging.info(f'User created successfully...')
            return Result(status=Status.CREATED, message=str(user.id))
        except IntegrityError as e:
            return Result(status=Status.CONFLICT, message=str(e))
        except DataError as e:
            return Result(status=Status.UNPROCESSABLE_ENTITY, message=str(e))
        except Exception as e:
            return Result(status=Status.ERROR, message=str(e))

    def Login(self, request, context):
        request: UserData
        logging.info(f'Received request for login...')
        try:
            user: User = User.get(User.username == request.username)
        except User.DoesNotExist:
            return Result(status=Status.NOT_FOUND, message='User not found')
        if user.password != request.password:
            return Result(status=Status.NOT_FOUND, message='Wrong password')
        roles_list: list[UserRoles] = UserRoles.select().where(UserRoles.user_id == user.id)
        roles = [user_role.role_id.name for user_role in roles_list]
        return Result(status=Status.CREATED, message=get_token(user.id, roles))

    def ValidateToken(self, request, context):
        request: Token
        logging.info(f'Received request for validating token...')
        if request.token in black_list:
            return Result(status=Status.FORBIDDEN, message='Token blacklisted')

        status, message = decode_token(request.token)
        if status is not None:
            return Result(status=status, message=message)
        msg_to_send = {'id': message['sub'], 'roles': message['roles']}
        return Result(status=Status.OK, message=json.dumps(msg_to_send))

    def DestroyToken(self, request, context):
        request: Token
        logging.info(f'Received request for destroying token...')
        if request.token in black_list:
            return Result(status=Status.FORBIDDEN, message='Token blacklisted')

        status, message = decode_token(request.token)

        if status is None:
            add_to_black_list(message['jti'], request.token)
        return Result(status=Status.OK, message='Token destroyed')

    def DeleteUser(self, request, context):
        request: UserId
        logging.info(f'Received request for deleting user...')

        if request.token in black_list:
            return Result(status=Status.FORBIDDEN, message='Token blacklisted')

        status, message = decode_token(request.token)
        if status is not None:
            return Result(status=status, message=message)

        if message['sub'] != request.id:
            return Result(status=Status.FORBIDDEN, message='Forbidden')

        try:
            user = User.get(User.id == request.id)
            userRoles = UserRoles.get(UserRoles.user_id == request.id)
            userRoles.delete_instance()
            user.delete_instance()
            return Result(status=Status.OK, message='User deleted')
        except User.DoesNotExist or UserRoles.DoesNotExist:
            return Result(status=Status.NOT_FOUND, message='User not found')

    def UpdateUser(self, request, context):
        request: UserPatch
        logging.info(f'Received request for updating user...')

        if request.token in black_list:
            return Result(status=Status.FORBIDDEN, message='Token blacklisted')

        status, message = decode_token(request.token)
        if status is not None:
            return Result(status=status, message=message)

        if message['sub'] != request.id:
            return Result(status=Status.FORBIDDEN, message='Forbidden')

        try:
            user: User = User.get(User.id == message['sub'])
            if request.username:
                user.username = request.username
            if request.password:
                user.password = request.password

            if request.action_role and request.action_role not in ['add', 'remove']:
                return Result(status=Status.UNPROCESSABLE_ENTITY, message='Invalid action on role')
            if request.action_role and request.role:
                role_id: Role = Role.get(Role.name == request.role)
            elif request.action_role and not request.role:
                return Result(status=Status.UNPROCESSABLE_ENTITY, message='For role an action is required')
            with db.atomic() as transaction:
                try:
                    user.save()
                    if request.action_role == 'add':
                        UserRoles.create(user_id=user.id, role_id=role_id.id)
                    elif request.action_role == 'remove':
                        UserRoles.get(UserRoles.user_id == user.id, UserRoles.role_id == role_id.id).delete_instance()
                except Exception as e:
                    transaction.rollback()
                    if isinstance(e, IntegrityError):
                        return Result(status=Status.CONFLICT, message='User already has this role')
                    if isinstance(e, DataError):
                        return Result(status=Status.UNPROCESSABLE_ENTITY, message='Invalid user data')
                    return Result(status=Status.ERROR, message=str(e))
            # return the reponse when the transaction is successful
            return Result(status=Status.NO_CONTENT)
        except User.DoesNotExist:
            return Result(status=Status.NOT_FOUND, message='User not found')
        except Role.DoesNotExist:
            return Result(status=Status.UNPROCESSABLE_ENTITY, message='Invalid role')
