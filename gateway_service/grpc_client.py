import logging

import grpc

from service_stub.service_pb2 import UserData, UserBody, Token, UserId, UserPatch
from service_stub.service_pb2_grpc import IdmServiceStub


class GrpcClient:
    def __init__(self, host: str, port: int):
        self.host = host
        self.port = port
        self.channel = grpc.insecure_channel(f'{self.host}:{self.port}')
        self.stub = IdmServiceStub(self.channel)

    def _reconnect(self):
        if self.channel:
            self.channel.close()
        self.channel = grpc.insecure_channel(f'{self.host}:{self.port}')
        self.stub = IdmServiceStub(self.channel)

    def _call_with_reconnect(self, func, *args, **kwargs):
        try:
            return func(*args, **kwargs)
        except grpc.RpcError as e:
            if e.code() == grpc.StatusCode.UNAVAILABLE:
                self._reconnect()
                try:
                    return func(*args, **kwargs)
                except ValueError:
                    raise e
            else:
                raise e

    def login(self, user: UserData):
        return self._call_with_reconnect(self.stub.Login, user)

    def create_user(self, data: UserBody):
        return self._call_with_reconnect(self.stub.CreateUser, data)

    def validate_token(self, token: Token):
        return self._call_with_reconnect(self.stub.ValidateToken, token)

    def destroy_token(self, token: Token):
        return self._call_with_reconnect(self.stub.DestroyToken, token)

    def delete_user(self, user_id: UserId):
        return self._call_with_reconnect(self.stub.DeleteUser, user_id)

    def update_user(self, user_patch: UserPatch):
        return self._call_with_reconnect(self.stub.UpdateUser, user_patch)
