package com.exam.lifetext_test.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Relationship {
    Bố(1), Mẹ(2), Anh(3), Chị(4), Em(5);

    private final int code;

    Relationship(int code) {
        this.code = code;
    }

    public static Relationship parseCode(int code){
        for (Relationship relationship: Relationship.values()){
            if(relationship.getCode() == code){
                return relationship;
            }
        }
       return null;
    }
}
