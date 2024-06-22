from peewee import ForeignKeyField, CompositeKey

from model.base_model import BaseModel
from model.role import Role
from model.user import User


class UserRoles(BaseModel):
    user_id = ForeignKeyField(User)
    role_id = ForeignKeyField(Role)

    class Meta:
        primary_key = CompositeKey("user_id", "role_id")
        db_table = "users_roles"
