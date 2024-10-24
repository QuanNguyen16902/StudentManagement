package com.exam.lifetext_test.payload;

import com.exam.lifetext_test.model.Gender;
import com.exam.lifetext_test.model.Relationship;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PersonalInformationResponse {
    private String fullName;

    private Integer relationship;

//    @DateTimeFormat(pattern = "dd/MM/yyyy")
//    @JsonFormat(pattern = "dd/MM/yyyy")
    private String birthDate;

    private Integer gender;
}
