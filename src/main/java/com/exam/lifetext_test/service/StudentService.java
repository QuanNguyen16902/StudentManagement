package com.exam.lifetext_test.service;

import com.exam.lifetext_test.model.Gender;
import com.exam.lifetext_test.model.PersonalInformation;
import com.exam.lifetext_test.model.Relationship;
import com.exam.lifetext_test.model.Student;
import com.exam.lifetext_test.payload.PersonalInformationResponse;
import com.exam.lifetext_test.payload.StudentRequest;
import com.exam.lifetext_test.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    public List<Student> getList(){
        return studentRepository.findAll();
    }
    public Student create(StudentRequest studentRequest){
        Student newStudent = new Student();
        newStudent.setFullName(studentRequest.getFullName());
        newStudent.setGender(Gender.parseCode(studentRequest.getGender()));
        newStudent.setBirthDate(studentRequest.getBirthDate());
        newStudent.setGPA(studentRequest.getGPA());
        newStudent.setPersonalInformations(studentRequest.getPersonalInformations().stream()
                .map(info -> {
                    PersonalInformation response = new PersonalInformation();
                    response.setFullName(info.getFullName());
                    response.setGender(Gender.parseCode(info.getGender()));
                    response.setRelationship(Relationship.parseCode(info.getRelationship()));
                    response.setBirthDate(info.getBirthDate());
                    response.setStudent(newStudent);
                    return response;
                })
                .collect(Collectors.toList()));
        return studentRepository.save(newStudent);
    }
    public Student getStudent(long id){
        return studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với ID: " + id));
    }

    public Student updateStudent(long id, StudentRequest studentRequest){
        Student updatedStudent = studentRepository.findById(id).orElseThrow(() -> new RuntimeException(("Không tìm thấy sinh vien với ID: " + id)));
        updatedStudent.setFullName(studentRequest.getFullName());
        updatedStudent.setBirthDate(studentRequest.getBirthDate());
        updatedStudent.setGender(Gender.parseCode(studentRequest.getGender()));
        updatedStudent.setPersonalInformations(studentRequest.getPersonalInformations().stream()
                .map(info -> {
                    PersonalInformation response = new PersonalInformation();
                    response.setFullName(info.getFullName());
                    response.setGender(Gender.parseCode(info.getGender()));
                    response.setRelationship(Relationship.parseCode(info.getRelationship()));
                    response.setBirthDate(info.getBirthDate());
                    response.setStudent(updatedStudent);
                    return response;
                })
                .collect(Collectors.toList()));

//        List<PersonalInformation> personalInformations = studentRequest.getPersonalInformations();
//        if(personalInformations != null){
//            updatedStudent.setPersonalInformations(personalInformations.stream()
//                    .map(info -> {
//                        PersonalInformation personalInformation = new PersonalInformation();
//                        personalInformation.setFullName(info.getFullName());
//                    }));
//        }

        return updatedStudent;
    }
    public void deleteStudent(long id){
        if(!studentRepository.existsById(id)){
            throw new RuntimeException("Không tồn tại sinh viên với id: " + id);
        }
        studentRepository.deleteById(id);
    }
}
