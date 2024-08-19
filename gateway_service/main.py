import json
import logging

import grpc
import httpx
from fastapi import FastAPI, Request
from google.protobuf.json_format import Parse, MessageToJson, ParseError
from starlette.middleware.cors import CORSMiddleware
from starlette.responses import Response

from grpc_client import GrpcClient
from service_stub.service_pb2 import UserData, UserBody, Token, UserId, UserPatch

grpc_client = GrpcClient(host="localhost", port=50051)


def get_grpc_client():
    return grpc_client


app = FastAPI()

SQL_SERVER_ENDPOINT = "http://127.0.0.1:8080/api/medical_office"
NOSQL_SERVER_ENDPOINT = "http://127.0.0.1:8081/api/medical_office"
NOSQL_PREFIXES = ["consultations"]
SQL_PREFIXES = ["patients", "physicians", 'appointments']

origins = [
    "http://localhost:3000",
]


app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


def get_redirect_url(path: str):
    for prefix in NOSQL_PREFIXES:
        if path.startswith(prefix):
            return f'{NOSQL_SERVER_ENDPOINT}/{path}'

    for prefix in SQL_PREFIXES:
        if path.startswith(prefix):
            return f'{SQL_SERVER_ENDPOINT}/{path}'
    return ''


@app.get("/api/medical_office/{path:path}")
@app.post("/api/medical_office/{path:path}")
@app.put("/api/medical_office/{path:path}")
@app.delete("/api/medical_office/{path:path}")
@app.patch("/api/medical_office/{path:path}")
async def redirect_to_other_server(path: str, request: Request):
    logging.info(path, request)
    query_params = dict(request.query_params)
    redirect_url = get_redirect_url(path)
    try:
        async def forward_request():
            async with httpx.AsyncClient() as client:
                body_content = await request.body()
                response = await client.request(
                    method=request.method,
                    url=redirect_url,
                    headers=request.headers,
                    params=query_params,
                    content=body_content,
                )
            return response

        server_response = await forward_request()
        return Response(
            content=server_response.content,
            status_code=server_response.status_code,
            headers=dict(server_response.headers),
        )
    except httpx.ConnectError:
        logging.info("Could not connect to target server... Service Unavailable")
        body = json.dumps({'message': "Service Unavailable"})
        return Response(body, media_type="application/json", status_code=503)


def create_response(body, status_code):
    return Response(body, media_type="application/json", status_code=status_code)


def create_body(body):
    return json.dumps({'message': body})


@app.post("/idm/tokens")
async def login(request: Request):
    data_json = await request.json()
    try:
        data = UserData()
        Parse(json.dumps(data_json), data)
        result = grpc_client.login(data)
        return create_response(MessageToJson(result), result.status)
    except ParseError as e:
        return create_response(create_body(e), 422)
    except grpc.RpcError as rpc_error:
        if rpc_error.code() == grpc.StatusCode.UNAVAILABLE:
            return create_response(create_body('Service unavailable'), 503)
        return create_response(create_body(rpc_error), 500)


@app.post("/idm/users")
async def create_user(request: Request):
    data_json = await request.json()
    token = request.headers.get("Authorization")
    if token is not None:
        token = token.split(" ")[1]
        data_json['token'] = token
    try:
        data = UserBody()
        Parse(json.dumps(data_json), data)
        result = grpc_client.create_user(data)
        return create_response(MessageToJson(result), result.status)
    except ParseError as e:
        return create_response(create_body(e), 422)
    except grpc.RpcError as rpc_error:
        if rpc_error.code() == grpc.StatusCode.UNAVAILABLE:
            return create_response(create_body('Service unavailable'), 503)
        return create_response(create_body(rpc_error), 500)


@app.delete("/idm/users/{id_user}")
async def destroy_user(id_user: int, request: Request):
    token = request.headers.get("Authorization")
    if token is not None:
        token = token.split(" ")[1]
    else:
        return create_response(create_body('Unauthorized'), 401)
    try:
        result = grpc_client.delete_user(UserId(id=id_user, token=token))
        return create_response(MessageToJson(result), result.status)
    except grpc.RpcError as rpc_error:
        if rpc_error.code() == grpc.StatusCode.UNAVAILABLE:
            return create_response(create_body('Service unavailable'), 503)
        return create_response(create_body(rpc_error), 500)


@app.patch("/idm/users/{id_user}")
async def update_user(id_user: int, request: Request):
    try:
        data_json = await request.json()
        token = request.headers.get("Authorization")
        if token is not None:
            token = token.split(" ")[1]
        else:
            return create_response(create_body('Unauthorized'), 401)
        data_json['id'] = id_user
        data_json['token'] = token
        data = UserPatch()
        Parse(json.dumps(data_json), data)
        result = grpc_client.update_user(data)
        return create_response(MessageToJson(result), result.status)
    except ParseError as e:
        return create_response(create_body(e), 422)
    except grpc.RpcError as rpc_error:
        if rpc_error.code() == grpc.StatusCode.UNAVAILABLE:
            return create_response(create_body('Service unavailable'), 503)
        return create_response(create_body(rpc_error), 500)


@app.get("/idm/tokens")
async def validate_token(request: Request):
    try:
        data_json = await request.json()
        data = Token()
        Parse(json.dumps(data_json), data)
        result = grpc_client.validate_token(data)
        return create_response(MessageToJson(result), 200)
    except ParseError as e:
        return create_response(create_body(e), 422)
    except grpc.RpcError as rpc_error:
        if rpc_error.code() == grpc.StatusCode.UNAVAILABLE:
            return create_response(create_body('Service unavailable'), 503)
        return create_response(create_body(rpc_error), 500)


@app.post("/idm/logout")
async def destroy_token(request: Request):
    try:
        data_json = await request.json()
        data = Token()
        Parse(json.dumps(data_json), data)
        result = grpc_client.stub.DestroyToken(data)
        return create_response(MessageToJson(result), 200)
    except ParseError as e:
        return create_response(create_body(e), 422)
    except grpc.RpcError as rpc_error:
        if rpc_error.code() == grpc.StatusCode.UNAVAILABLE:
            return create_response(create_body('Service unavailable'), 503)
        return create_response(create_body(rpc_error), 500)
