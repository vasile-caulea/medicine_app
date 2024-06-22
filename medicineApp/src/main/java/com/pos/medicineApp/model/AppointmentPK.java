package com.pos.medicineApp.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentPK implements Serializable {

    private String patient;
    private Integer physician;
    private Date date;
}
