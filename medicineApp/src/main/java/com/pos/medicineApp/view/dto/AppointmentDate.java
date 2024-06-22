package com.pos.medicineApp.view.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AppointmentDate {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Bucharest")
    Date date;
}
