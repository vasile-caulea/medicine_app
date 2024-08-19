package com.pos.medicineConsults.model.POJO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("investigations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Investigation {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @Field(name = "name")
    @NotBlank
    private String name;

    @Field(name = "duration")
    @NotNull
    private Integer duration;

    @Field(name = "result")
    @NotBlank
    private String result;
}
