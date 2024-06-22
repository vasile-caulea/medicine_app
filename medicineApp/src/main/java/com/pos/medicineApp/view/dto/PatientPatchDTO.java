package com.pos.medicineApp.view.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class PatientPatchDTO {
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;

    @JsonIgnore
    public boolean isValid() {
        return phoneNumber != null || email != null || lastName != null || firstName != null;
    }
}
