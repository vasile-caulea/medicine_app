import logging
import sys
from concurrent import futures

import grpc

from database_connection.database import db
from model.role import Role
from model.user import User
from model.user_roles import UserRoles
from service.service import IdmService
from service_stub import service_pb2_grpc
from utils import init_black_list, save_black_list

if __name__ == '__main__':

    for handler in logging.getLogger().handlers[:]:
        logging.getLogger().removeHandler(handler)

    formatter = logging.Formatter('%(levelname)s:%(name)s - %(asctime)s - %(message)s', datefmt='%d-%b-%y %H:%M:%S')
    console_handler = logging.StreamHandler(sys.stdout)
    console_handler.setFormatter(formatter)
    file_handler = logging.FileHandler('app.log')
    file_handler.setFormatter(formatter)
    logging.getLogger().addHandler(console_handler)
    logging.getLogger().addHandler(file_handler)
    logging.getLogger().setLevel(logging.INFO)

    db.connect()
    db.create_tables([User, Role, UserRoles])

    init_black_list()

    try:
        server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
        service_pb2_grpc.add_IdmServiceServicer_to_server(IdmService(), server)
        server.add_insecure_port('[::]:50051')
        server.start()
        logging.info('Starting server. Listening on port 50051...')
        server.wait_for_termination()
    except KeyboardInterrupt:
        save_black_list()

    save_black_list()
