import grpc

from service_stub import service_pb2_grpc
from service_stub.service_pb2 import UserData

if __name__ == '__main__':
    channel = grpc.insecure_channel('localhost:50051')

    stub = service_pb2_grpc.IdmServiceStub(channel)

    # creating users

    # user_data = UserData(username='admin', password='admin')
    # user = User(data=user_data, role='admin')
    # response = stub.CreateUser(user)
    # print(f"Status {response.status}")
    # print(f"Message {response.message}")
    #
    # user_data = UserData(username='admin3', password='admin')
    # user = User(data=user_data, role='admin')
    # response = stub.CreateUser(user)
    # print(f"Status {response.status}")
    # print(f"Message {response.message}")

    # login
    user_data = UserData(username='admin', password='admin')
    response = stub.Login(user_data)
    print(f"Status {response.status}")
    print(f"Message {response.message}")
    #
    # # verify token
    # token = response.message
    #
    # # sleep for 30 seconds so token will expire
    # print('Sleeping for 5 seconds...')
    # time.sleep(7)
    #
    # verif_token: Result = stub.ValidateToken(Token(token=response.message))
    # if verif_token.status == Status.OK:
    #     print(f'Token is valid {verif_token.message}')
    # else:
    #     print(f'{verif_token.message}')

