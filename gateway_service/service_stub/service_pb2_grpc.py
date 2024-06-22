# Generated by the gRPC Python protocol compiler plugin. DO NOT EDIT!
"""Client and server classes corresponding to protobuf-defined services."""
import grpc

from service_stub import service_pb2 as service__stub_dot_service__pb2


class IdmServiceStub(object):
    """Missing associated documentation comment in .proto file."""

    def __init__(self, channel):
        """Constructor.

        Args:
            channel: A grpc.Channel.
        """
        self.CreateUser = channel.unary_unary(
                '/idm.IdmService/CreateUser',
                request_serializer=service__stub_dot_service__pb2.UserBody.SerializeToString,
                response_deserializer=service__stub_dot_service__pb2.Result.FromString,
                )
        self.Login = channel.unary_unary(
                '/idm.IdmService/Login',
                request_serializer=service__stub_dot_service__pb2.UserData.SerializeToString,
                response_deserializer=service__stub_dot_service__pb2.Result.FromString,
                )
        self.ValidateToken = channel.unary_unary(
                '/idm.IdmService/ValidateToken',
                request_serializer=service__stub_dot_service__pb2.Token.SerializeToString,
                response_deserializer=service__stub_dot_service__pb2.Result.FromString,
                )
        self.DestroyToken = channel.unary_unary(
                '/idm.IdmService/DestroyToken',
                request_serializer=service__stub_dot_service__pb2.Token.SerializeToString,
                response_deserializer=service__stub_dot_service__pb2.Result.FromString,
                )
        self.DeleteUser = channel.unary_unary(
                '/idm.IdmService/DeleteUser',
                request_serializer=service__stub_dot_service__pb2.UserId.SerializeToString,
                response_deserializer=service__stub_dot_service__pb2.Result.FromString,
                )
        self.UpdateUser = channel.unary_unary(
                '/idm.IdmService/UpdateUser',
                request_serializer=service__stub_dot_service__pb2.UserPatch.SerializeToString,
                response_deserializer=service__stub_dot_service__pb2.Result.FromString,
                )


class IdmServiceServicer(object):
    """Missing associated documentation comment in .proto file."""

    def CreateUser(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def Login(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def ValidateToken(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def DestroyToken(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def DeleteUser(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def UpdateUser(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')


def add_IdmServiceServicer_to_server(servicer, server):
    rpc_method_handlers = {
            'CreateUser': grpc.unary_unary_rpc_method_handler(
                    servicer.CreateUser,
                    request_deserializer=service__stub_dot_service__pb2.UserBody.FromString,
                    response_serializer=service__stub_dot_service__pb2.Result.SerializeToString,
            ),
            'Login': grpc.unary_unary_rpc_method_handler(
                    servicer.Login,
                    request_deserializer=service__stub_dot_service__pb2.UserData.FromString,
                    response_serializer=service__stub_dot_service__pb2.Result.SerializeToString,
            ),
            'ValidateToken': grpc.unary_unary_rpc_method_handler(
                    servicer.ValidateToken,
                    request_deserializer=service__stub_dot_service__pb2.Token.FromString,
                    response_serializer=service__stub_dot_service__pb2.Result.SerializeToString,
            ),
            'DestroyToken': grpc.unary_unary_rpc_method_handler(
                    servicer.DestroyToken,
                    request_deserializer=service__stub_dot_service__pb2.Token.FromString,
                    response_serializer=service__stub_dot_service__pb2.Result.SerializeToString,
            ),
            'DeleteUser': grpc.unary_unary_rpc_method_handler(
                    servicer.DeleteUser,
                    request_deserializer=service__stub_dot_service__pb2.UserId.FromString,
                    response_serializer=service__stub_dot_service__pb2.Result.SerializeToString,
            ),
            'UpdateUser': grpc.unary_unary_rpc_method_handler(
                    servicer.UpdateUser,
                    request_deserializer=service__stub_dot_service__pb2.UserPatch.FromString,
                    response_serializer=service__stub_dot_service__pb2.Result.SerializeToString,
            ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
            'idm.IdmService', rpc_method_handlers)
    server.add_generic_rpc_handlers((generic_handler,))


 # This class is part of an EXPERIMENTAL API.
class IdmService(object):
    """Missing associated documentation comment in .proto file."""

    @staticmethod
    def CreateUser(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/idm.IdmService/CreateUser',
            service__stub_dot_service__pb2.UserBody.SerializeToString,
            service__stub_dot_service__pb2.Result.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def Login(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/idm.IdmService/Login',
            service__stub_dot_service__pb2.UserData.SerializeToString,
            service__stub_dot_service__pb2.Result.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def ValidateToken(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/idm.IdmService/ValidateToken',
            service__stub_dot_service__pb2.Token.SerializeToString,
            service__stub_dot_service__pb2.Result.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def DestroyToken(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/idm.IdmService/DestroyToken',
            service__stub_dot_service__pb2.Token.SerializeToString,
            service__stub_dot_service__pb2.Result.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def DeleteUser(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/idm.IdmService/DeleteUser',
            service__stub_dot_service__pb2.UserId.SerializeToString,
            service__stub_dot_service__pb2.Result.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def UpdateUser(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/idm.IdmService/UpdateUser',
            service__stub_dot_service__pb2.UserPatch.SerializeToString,
            service__stub_dot_service__pb2.Result.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)