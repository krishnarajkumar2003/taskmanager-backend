package com.cogniwide.TaskManager.customException;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{
    @Getter
    HttpStatus statusCode;
    public CustomException(HttpStatus id,String message){
        super(message);
        this.statusCode=id;
    }
}
