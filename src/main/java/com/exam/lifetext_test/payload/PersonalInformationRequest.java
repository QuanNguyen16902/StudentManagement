package com.exam.lifetext_test.payload;

import com.exam.lifetext_test.model.Gender;
import com.exam.lifetext_test.model.Relationship;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class PersonalInformationRequest {
    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    @NotNull(message = "Mối quan hệ không được để trống")
    private Integer relationship;

    @NotBlank(message = "Ngày tháng năm sinh không được để trống")
    private String birthDate;

    @NotNull(message = "Giới tính không được để trống")
    private Integer gender;
}
