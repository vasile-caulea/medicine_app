import base64
import enum
import json
import logging
import uuid
from datetime import datetime, timedelta

import jwt

ISS = 'http://127.0.0.1:8000'
KEY = 'encode_key'
JWS_ALG = 'HS256'

black_list: dict[str, str] = {}
CONTAINER_NAME = 'idm_container'


class Status(enum.IntEnum):
    OK = 200
    CREATED = 201
    NO_CONTENT = 204
    UNAUTHORIZED = 401
    FORBIDDEN = 403
    NOT_FOUND = 404
    CONFLICT = 409
    UNPROCESSABLE_ENTITY = 422
    ERROR = 500


def init_black_list() -> None:
    global black_list
    try:
        with open('black_list.json', 'r') as f:
            black_list = json.load(f)
    except FileNotFoundError:
        save_black_list()


def save_black_list() -> None:
    with open('black_list.json', 'w') as f:
        json.dump(black_list, f)


def get_token(id_user, roles: list) -> str:
    payload = {
        'iss': ISS,
        'sub': id_user,
        'exp': datetime.utcnow() + timedelta(hours=1),  # timedelta(hours=1)
        'jti': str(uuid.uuid4()),
        'roles': roles
    }
    return jwt.encode(payload, KEY, algorithm=JWS_ALG)


def add_to_black_list(uuid_str: str, token: str):
    if uuid_str:
        black_list[uuid_str] = token
    else:
        try:
            header, payload, signature = token.split(".")
            # add padding if needed to make the length of payload a multiple of 4
            payload += '=' * (-len(payload) % 4)
            payload = base64.urlsafe_b64decode(payload)
            payload = payload.decode('utf-8')
            payload = json.loads(payload)
            black_list[payload['jti']] = token
        except Exception as e:
            logging.info(f'Could not add to black list {token}. error = {e}', exc_info=True)


def decode_token(token: str) -> tuple:
    try:
        payload = jwt.decode(token, KEY, algorithms=[JWS_ALG])
    except jwt.exceptions.ExpiredSignatureError:
        add_to_black_list('', token)
        return Status.UNAUTHORIZED, 'Token expired'
    except jwt.exceptions.InvalidTokenError:
        add_to_black_list('', token)
        return Status.UNAUTHORIZED, 'Invalid token'

    # check issuer
    if payload['iss'] != ISS:
        return Status.UNAUTHORIZED, 'Invalid issuer'

    return None, payload
