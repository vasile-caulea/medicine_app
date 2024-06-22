from peewee import Model

from database_connection.database import db


class BaseModel(Model):
    class Meta:
        database = db
