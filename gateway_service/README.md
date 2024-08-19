# Gateway service

This service provides a gateway that routes requests to the existent servers (medicine_app, medicine_consults, and idm).
The file <b>service.proto</b> defines the messages structures and the services provided by idm service.

## Dependencies
### External
  * fastapi - 0.112.1 - https://fastapi.tiangolo.com/
  * grpcio - 1.65.5 - https://grpc.github.io/grpc/python/
  * httpx - 0.27.0 - https://www.python-httpx.org/
  * protobuf - 5.27.3 - https://pypi.org/project/protobuf/
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

## How to run

Main app must be run using the following command:
``` shell
uvicorn main:app --reload
```