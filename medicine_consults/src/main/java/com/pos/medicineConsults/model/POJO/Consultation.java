package com.pos.medicineConsults.model.POJO;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Document("consultations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Consultation {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @Field(name = "id_patient")
    @NotBlank
    String idPatient;

    @Field(name = "id_physician")
    @NotNull
    Integer idPhysician;

    @Field(name = "date")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Bucharest")
    Date date;

    @Field(name = "diagnosis")
    @Enumerated(EnumType.STRING)
    @NotBlank(message = "invalid diagnostic")
    Diagnosis diagnosis;

    @Field(name = "investigations")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Investigation> investigations = new LinkedList<>();
}
