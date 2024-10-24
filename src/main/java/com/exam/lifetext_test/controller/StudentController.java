package com.exam.lifetext_test.controller;

import com.exam.lifetext_test.model.Relationship;
import com.exam.lifetext_test.model.Student;
import com.exam.lifetext_test.payload.*;
import com.exam.lifetext_test.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {

    private StudentService studentService;
    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> listAll(){
        List<Student> studentList = studentService.getList();
        return ResponseEntity.ok(studentList);
    }

    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentRequest studentRequest){
        List<String> errors = validateRequest(studentRequest);
        // Nếu có lỗi, trả về lỗi
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(errors));
        }
        else{
            Student newStudent = studentService.create(studentRequest);
            StudentResponse studentResponse = new StudentResponse();
            studentResponse.setFullName(newStudent.getFullName());
            studentResponse.setBirthDate(newStudent.getBirthDate());
            studentResponse.setGender((newStudent.getGender().getCode()));
            studentResponse.setPersonalInformations(newStudent.getPersonalInformations().stream()
                    .map(info -> {
                        PersonalInformationResponse response = new PersonalInformationResponse();
                        response.setFullName(info.getFullName());
                        response.setGender(info.getGender().getCode());
                        response.setRelationship(info.getRelationship().getCode());
                        response.setBirthDate(info.getBirthDate());
                        return response;
                    })
                    .collect(Collectors.toList()));
            studentResponse.setGPA(newStudent.getGPA());
            return ResponseEntity.status(HttpStatus.CREATED).body(studentResponse);

        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable long id){
        Student student = studentService.getStudent(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new StudentResponse(student.getFullName(), student.getBirthDate(), student.getGender().getCode()
                    ,student.getPersonalInformations().stream().map(info -> {
                        PersonalInformationResponse response = new PersonalInformationResponse();
                    response.setFullName(info.getFullName());
                    response.setGender(info.getGender().getCode());
                    response.setRelationship(info.getRelationship().getCode());
                    response.setBirthDate(info.getBirthDate());
                    return response;
                }).collect(Collectors.toList()), student.getGPA()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable long id){
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Xóa sinh viên với ID " + id +" thành công");
    }

    private List<String> validateRequest(StudentRequest studentRequest){
        // Validate dữ liệu
        List<String> errors = new ArrayList<>();
        if (studentRequest.getFullName() == null || studentRequest.getFullName().trim().isEmpty()) {
            errors.add("Họ và tên không được để trống.");
        }
        // Ngày
        if (studentRequest.getBirthDate() == null ) {
            errors.add("Ngày tháng năm sinh không được để trống.");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String birthDateString = studentRequest.getBirthDate();
            System.out.println(birthDateString);
            if (birthDateString.matches("\\d{2}/\\d{2}/\\d{4}")) {
                try {
                    System.out.println("Đúng định dạng");
                    LocalDate.parse(birthDateString, formatter);
                } catch (DateTimeParseException e) {
                    errors.add("Ngày tháng năm sinh không hợp lệ.");
                }
            } else {
                errors.add("Ngày tháng năm sinh phải có định dạng dd/MM/yyyy.");
            }
        }
        if (studentRequest.getGender() == null) {
            errors.add("Giới tính không được để trống.");
        }else{
            if (studentRequest.getGender() != 1 && studentRequest.getGender() != 2) {
                errors.add("Mã giới tính không hợp lệ, hãy điền 1(Nam) hoặc 2(Nữ)");
            }
        }
        if (studentRequest.getPersonalInformations() == null || studentRequest.getPersonalInformations().isEmpty()) {
            errors.add("Thông tin người thân không được để trống.");
        } else {
            int i = 1;
            for (PersonalInformationRequest info : studentRequest.getPersonalInformations()) {
                if (info.getFullName() == null || info.getFullName().trim().isEmpty()) {
                    errors.add("Họ và tên của người thân " + i +" không được để trống.");
                }
                if (info.getRelationship() == null) {
                    errors.add("Mối quan hệ " + i + " không được để trống.");
                }else {
                    Relationship relationship = Relationship.parseCode(info.getRelationship());
                    if(relationship == null){
                        errors.add("Mã mối quan hệ của người thân " + i + " ko hợp lệ! (1,2,3,4,5)");
                    }
                }
                // Kiểm tra ngày tháng năm sinh của người thân
                if (info.getBirthDate() == null) {
                    errors.add("Ngày tháng năm sinh của người thân " + i + " không được để trống.");
                }else{
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String birthDateString = info.getBirthDate();
                    if (birthDateString.matches("\\d{2}/\\d{2}/\\d{4}")) {
                        try {
                            LocalDate.parse(birthDateString, formatter);
                        } catch (DateTimeParseException e) {
                            errors.add("Ngày tháng năm sinh của người thân " + i + " không hợp lệ.");
                        }
                    } else {
                        errors.add("Ngày tháng năm sinh của người thân " + i + " phải có định dạng dd/MM/yyyy.");
                    }
                }
                // Kiểm tra giới tính của người thân
                if (info.getGender() == null) {
                    errors.add("Giới tính của người thân " + i + " không được để trống.");
                }else{
                    if (info.getGender() != 1 && info.getGender() != 2) {
                        errors.add("Mã giới tính của người thân " + i +" không hợp lệ, hãy điền 1(Nam) hoặc 2(Nữ)");
                    }
                }
                i = i + 1;
            }
        }
        return errors;
    }

}
