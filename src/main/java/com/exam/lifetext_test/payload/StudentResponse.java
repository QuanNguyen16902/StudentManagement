package com.exam.lifetext_test.payload;

import com.exam.lifetext_test.model.Gender;
import com.exam.lifetext_test.model.PersonalInformation;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    private String fullName;

//    @JsonFormat(pattern = "dd/MM/yyyy")
    private String birthDate;

    private Integer gender;

    private List<PersonalInformationResponse> personalInformations;

    private float GPA;
}
