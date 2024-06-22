package com.pos.medicineApp.view.dto;

import com.pos.medicineApp.model.AppointmentStatus;

public enum PhysicianAppointmentStatus {
    ATTENDED,
    NATTENDED;


    public AppointmentStatus getAppointmentStatus() {
        return switch (this) {
            case ATTENDED -> AppointmentStatus.ATTENDED;
            case NATTENDED -> AppointmentStatus.NATTENDED;
        };
    }
}