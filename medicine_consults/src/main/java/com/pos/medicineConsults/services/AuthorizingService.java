package com.pos.medicineConsults.services;

import com.pos.medicineConsults.exceptions.ForbiddenException;
import com.pos.medicineConsults.interfaces.services.IAuthorizingService;
import com.pos.medicineConsults.utils.IDMUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.pos.medicineConsults.utils.Utils.extractToken;
import static com.pos.medicineConsults.utils.Utils.getUserInfo;


@Service
@RequiredArgsConstructor
public class AuthorizingService implements IAuthorizingService {

    private final IDMClientService idmClientService;

    private IDMUserInfo getUserInfoFromToken(Map<String, String> headers) {
        String token = extractToken(headers);
        String userInfo = idmClientService.validateToken(token);
        return getUserInfo(userInfo);
    }

    @Override
    public void authorizeRolePhysician(Map<String, String> headers) {
        IDMUserInfo user = getUserInfoFromToken(headers);
        if (!user.getRoles().contains("physician")) {
            throw new ForbiddenException();
        }
    }

    @Override
    public IDMUserInfo authorizeRolePhysicianPatient(Map<String, String> headers) {
        IDMUserInfo user = getUserInfoFromToken(headers);
        if (!user.getRoles().contains("physician") && !user.getRoles().contains("patient")) {
            throw new ForbiddenException();
        }
        return user;
    }
}
