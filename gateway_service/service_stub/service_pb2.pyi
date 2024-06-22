from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Optional as _Optional

DESCRIPTOR: _descriptor.FileDescriptor

class UserPatch(_message.Message):
    __slots__ = ("token", "id", "username", "password", "action_role", "role")
    TOKEN_FIELD_NUMBER: _ClassVar[int]
    ID_FIELD_NUMBER: _ClassVar[int]
    USERNAME_FIELD_NUMBER: _ClassVar[int]
    PASSWORD_FIELD_NUMBER: _ClassVar[int]
    ACTION_ROLE_FIELD_NUMBER: _ClassVar[int]
    ROLE_FIELD_NUMBER: _ClassVar[int]
    token: str
    id: int
    username: str
    password: str
    action_role: str
    role: str
    def __init__(self, token: _Optional[str] = ..., id: _Optional[int] = ..., username: _Optional[str] = ..., password: _Optional[str] = ..., action_role: _Optional[str] = ..., role: _Optional[str] = ...) -> None: ...

class Result(_message.Message):
    __slots__ = ("status", "message")
    STATUS_FIELD_NUMBER: _ClassVar[int]
    MESSAGE_FIELD_NUMBER: _ClassVar[int]
    status: int
    message: str
    def __init__(self, status: _Optional[int] = ..., message: _Optional[str] = ...) -> None: ...

class UserId(_message.Message):
    __slots__ = ("token", "id")
    TOKEN_FIELD_NUMBER: _ClassVar[int]
    ID_FIELD_NUMBER: _ClassVar[int]
    token: str
    id: int
    def __init__(self, token: _Optional[str] = ..., id: _Optional[int] = ...) -> None: ...

class UserData(_message.Message):
    __slots__ = ("username", "password")
    USERNAME_FIELD_NUMBER: _ClassVar[int]
    PASSWORD_FIELD_NUMBER: _ClassVar[int]
    username: str
    password: str
    def __init__(self, username: _Optional[str] = ..., password: _Optional[str] = ...) -> None: ...

class UserBody(_message.Message):
    __slots__ = ("token", "username", "password", "role")
    TOKEN_FIELD_NUMBER: _ClassVar[int]
    USERNAME_FIELD_NUMBER: _ClassVar[int]
    PASSWORD_FIELD_NUMBER: _ClassVar[int]
    ROLE_FIELD_NUMBER: _ClassVar[int]
    token: str
    username: str
    password: str
    role: str
    def __init__(self, token: _Optional[str] = ..., username: _Optional[str] = ..., password: _Optional[str] = ..., role: _Optional[str] = ...) -> None: ...

class Token(_message.Message):
    __slots__ = ("token",)
    TOKEN_FIELD_NUMBER: _ClassVar[int]
    token: str
    def __init__(self, token: _Optional[str] = ...) -> None: ...
