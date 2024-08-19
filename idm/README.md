# IDM service

This project provides a server that handles the Identity Management services of the final application, and it is based on gRPC methods.

## Dependencies

Activate venv

```shell
. ./venv/bin/activate
```

### External

* grpcio - 1.65.5 - https://grpc.github.io/grpc/python/
* peewee - 3.17.6 - https://pypi.org/project/peewee/
* protobuf - 5.27.3 - https://pypi.org/project/protobuf/
* python-jwt - 1.3.1 - https://pypi.org/project/jwt/
* mysql - - https://pymysql.readthedocs.io/en/latest/

```shell
pip install grpcio==1.65.5
pip install peewee==3.17.6
pip install protobuf==5.27.3
pip install jwt==1.3.1
pip install PyMySQL==1.1.1
```

### Internal


In order to call the remote procedures, the project requires a python code which will be generated from file <b>service.proto</b>.

First, install grpc-tools using: 
``` shell
pip install grpcio-tools
```

After successful installation, run the following command to generate the python code.

``` shell
 python -m grpc_tools.protoc -I. --python_out=. --pyi_out=. --grpc_python_out=. ./service_stub/service.proto
```