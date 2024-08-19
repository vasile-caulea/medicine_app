package com.pos.medicineConsults.interfaces.services;

import com.pos.medicineConsults.utils.IDMUserInfo;

import java.util.Map;

public interface IAuthorizingService {
    void authorizeRolePhysician(Map<String, String> headers);

    IDMUserInfo authorizeRolePhysicianPatient(Map<String, String> headers);
}
