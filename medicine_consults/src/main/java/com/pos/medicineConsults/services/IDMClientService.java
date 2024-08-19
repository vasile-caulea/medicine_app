package com.pos.medicineConsults.services;

import com.pos.medicineApp.idm.IdmServiceGrpc;
import com.pos.medicineApp.idm.Result;
import com.pos.medicineApp.idm.Token;
import com.pos.medicineConsults.exceptions.ForbiddenException;
import com.pos.medicineConsults.exceptions.UnauthorizedException;
import com.pos.medicineConsults.interfaces.services.IIDMClientService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class IDMClientService implements IIDMClientService {

    @Value("${idm.server.name}")
    private String serverName;

    @Value("${idm.server.port}")
    private int serverPort;

    private ManagedChannel channel;
    private IdmServiceGrpc.IdmServiceBlockingStub stub;

    public IDMClientService() {
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        channel = ManagedChannelBuilder.forAddress(serverName, serverPort).usePlaintext().build();
        stub = IdmServiceGrpc.newBlockingStub(channel);
    }

    @PreDestroy
    public void shutDown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
    }

    /**
     * Validates the given token.
     *
     * @param  token  the token to be validated
     * @return        the message returned by the validation
     * @throws        UnauthorizedException  if the token is invalid/expired
     * @throws        ForbiddenException     if the token is valid but not authorized
     */
    public String validateToken(String token) {
        Result result = stub.validateToken(Token.newBuilder().setToken(token).build());
        if (result.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
            throw new UnauthorizedException(result.getMessage());
        }
        if (result.getStatus() == HttpStatus.FORBIDDEN.value()) {
            throw new ForbiddenException(result.getMessage());
        }
        return result.getMessage();
    }
}
