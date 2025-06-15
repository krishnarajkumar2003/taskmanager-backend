package com.cogniwide.TaskManager.DTO;

import com.cogniwide.TaskManager.enums.Category;
import com.cogniwide.TaskManager.enums.Status;
import com.cogniwide.TaskManager.enums.TaskPriority;
import lombok.Data;

@Data
public class UpdateTaskDTO {
    private String title;
    private Category category;
    private TaskPriority priority;
    private Status status;
}
