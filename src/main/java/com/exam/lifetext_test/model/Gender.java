package com.exam.lifetext_test.model;

import lombok.Getter;

@Getter
public enum Gender {
    Nam(1),
    Nữ(2);

    private int code;

    Gender(int code) {
        this.code = code;
    }

    public static Gender parseCode(int code){
        for(Gender gender : Gender.values()){
            if(gender.getCode() == code){
                return gender;
            }
        }
        return null;
    }
}
