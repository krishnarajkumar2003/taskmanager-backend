package com.cogniwide.TaskManager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerResponse<T> {
    int statusCode;
    T data;
    String message;
}
