package com.cogniwide.TaskManager.customException;
import com.cogniwide.TaskManager.ServerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ServerResponse<?>> customException(CustomException ex){
        String errorMsg = ex.getMessage();
        int statusCode = ex.getStatusCode().value();
        ServerResponse<String> response = new ServerResponse<>();
        response.setStatusCode(ex.getStatusCode().value());
        response.setData(null);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response,ex.statusCode);
    }
}