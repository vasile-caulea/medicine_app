from peewee import CharField, AutoField, Check

from model.base_model import BaseModel


class Role(BaseModel):
    id = AutoField(primary_key=True)
    name = CharField(null=False, unique=True, constraints=[Check('name IN ("admin", "patient", "physician")')])

    class Meta:
        db_table = 'roles'
