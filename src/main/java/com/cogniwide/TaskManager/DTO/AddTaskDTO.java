package com.cogniwide.TaskManager.DTO;

import com.cogniwide.TaskManager.enums.Category;
import com.cogniwide.TaskManager.enums.TaskPriority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddTaskDTO {
    private String title;
    private TaskPriority priority;
    private Category category;
}
