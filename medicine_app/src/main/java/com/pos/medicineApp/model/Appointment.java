package com.pos.medicineApp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "appointments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(AppointmentPK.class)
public class Appointment {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_physician")
    private Physician physician;

    @Id
    @Column(name = "date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Bucharest")
    private Date date;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;
}