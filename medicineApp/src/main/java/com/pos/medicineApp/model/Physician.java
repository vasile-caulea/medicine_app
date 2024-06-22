package com.pos.medicineApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "physicians")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Physician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_physician")
    private Integer idPhysician;

    @Column(name = "id_user", nullable = false)
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

    @Column(name = "email", length = 70, unique = true, nullable = false)
    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is mandatory")
    @Size(max = 70, message = "Invalid email length")
    private String email;

    @Column(name = "phone_number", length = 10, unique = true, nullable = false)
    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "[0-9]{10}$")
    private String phoneNumber;

    @Column(name = "specialization", nullable = false)
    @NotBlank(message = "Specialization is mandatory")
    private String specialization;
}