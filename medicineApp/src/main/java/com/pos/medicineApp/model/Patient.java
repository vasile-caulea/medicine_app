package com.pos.medicineApp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.medicineApp.utils.validators.CnpValidation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "patients")
@Data
public class Patient {

    @Id
    @Column(name = "cnp", length = 13, nullable = false)
    @NotBlank
    @CnpValidation
    @Pattern(regexp = "[0-9]*", message = "CNP must contain only digits")
    @Size(min = 13, max = 13, message = "CNP has an invalid number of digits")
    private String cnp;

    @Column(name = "id_user")
    @NotNull
    private Integer idUser;

    @Column(name = "last_name", length = 50, nullable = false)
    @NotBlank(message = "Last name is mandatory")
    @Pattern(regexp = "^(\\p{L}+[- ]?)+$", message = "Invalid last name format")
    @Size(max = 50, message = "Invalid last name length")
    private String lastName;

    @Column(name = "first_name", length = 50, nullable = false)
    @NotBlank(message = "First name is mandatory")
    @Pattern(regexp = "^(\\p{L}+[- ]?)+$", message = "Invalid first name format")
    @Size(max = 50, message = "Invalid first name length")
    private String firstName;

    @Column(name = "email", unique = true, length = 70, nullable = false)
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email address")
    @Size(max = 70, message = "Invalid email length")
    private String email;

    @Column(name = "phone_number", length = 10, unique = true, nullable = false)
    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "[0-9]{10}$")
    private String phoneNumber;

    @Column(name = "birth_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;

    @JsonIgnore
    @Column(name = "is_active")
    private Boolean isActive;
}
