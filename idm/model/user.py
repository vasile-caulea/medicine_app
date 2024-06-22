from peewee import CharField, AutoField

from model.base_model import BaseModel


class User(BaseModel):
    id = AutoField(primary_key=True)
    username = CharField(null=False, unique=True)
    password = CharField(null=False)

    class Meta:
        db_table = 'users'
