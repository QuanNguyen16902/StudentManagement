package com.exam.lifetext_test.payload;

import com.exam.lifetext_test.model.Gender;
import com.exam.lifetext_test.model.PersonalInformation;
import com.exam.lifetext_test.model.Relationship;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class StudentRequest {
    @NotBlank(message = "Họ và tên không được trống")
    private String fullName;

    @NotNull(message = "Ngày tháng năm sinh không được để trống")
//    @JsonFormat(pattern = "dd/MM/yyyy")
    private String birthDate;

    @NotNull(message = "Giới tính không được để trống")
    private Integer gender;

    private List<PersonalInformationRequest> personalInformations;

    @NotNull(message = "GPA không được để trống")
    private float GPA;
}
