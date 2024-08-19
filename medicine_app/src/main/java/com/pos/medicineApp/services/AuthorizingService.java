package com.pos.medicineApp.services;

import com.pos.medicineApp.exceptions.ForbiddenException;
import com.pos.medicineApp.interfaces.services.IAuthorizingService;
import com.pos.medicineApp.repository.PatientRepository;
import com.pos.medicineApp.repository.PhysicianRepository;
import com.pos.medicineApp.utils.IDMUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.pos.medicineApp.utils.Utils.extractToken;
import static com.pos.medicineApp.utils.Utils.getUserInfo;

@Service
@RequiredArgsConstructor
public class AuthorizingService implements IAuthorizingService {

    private final PatientRepository patientRepository;
    private final IDMClientService idmClientService;
    private final PhysicianRepository physicianRepository;


    private IDMUserInfo getUserInfoFromToken(Map<String, String> headers) {
        String token = extractToken(headers);
        String userInfo = idmClientService.validateToken(token);
        return getUserInfo(userInfo);
    }

    @Override
    public void authorizePatient(Map<String, String> headers, String patientId) {
        IDMUserInfo user = getUserInfoFromToken(headers);
        if (!patientRepository.existsByIdUserAndCnp(user.getId(), patientId)) {
            throw new ForbiddenException();
        }
    }

    @Override
    public void authorizePhysician(Map<String, String> headers, Integer idp) {
        IDMUserInfo user = getUserInfoFromToken(headers);
        if (!physicianRepository.existsByIdUserAndIdPhysician(user.getId(), idp)) {
            throw new ForbiddenException();
        }
    }

    @Override
    public void authorizePhysicianByUserId(Map<String, String> headers, Integer idUser) {
        IDMUserInfo user = getUserInfoFromToken(headers);
        if (!user.getId().equals(idUser)) {
            throw new ForbiddenException();
        }
    }

    @Override
    public void authorizePatientByUserId(Map<String, String> headers, Integer userId) {
        IDMUserInfo user = getUserInfoFromToken(headers);
        if (!user.getId().equals(userId)) {
            throw new ForbiddenException();
        }
    }

    @Override
    public void authorizeCreatePhysician(Map<String, String> headers, Integer idUser) {
        IDMUserInfo user = getUserInfoFromToken(headers);
        if (!user.getId().equals(idUser) || !user.getRoles().contains("physician")) {
            throw new ForbiddenException();
        }
    }

    @Override
    public void authorizeCreatePatient(Map<String, String> headers, Integer idUser) {
        IDMUserInfo user = getUserInfoFromToken(headers);
        if (!user.getId().equals(idUser) && !user.getRoles().contains("patient")) {
            throw new ForbiddenException();
        }
    }
}
