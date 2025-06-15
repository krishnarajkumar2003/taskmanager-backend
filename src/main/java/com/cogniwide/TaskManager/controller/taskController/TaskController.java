package com.cogniwide.TaskManager.controller.taskController;

import com.cogniwide.TaskManager.DTO.AddTaskDTO;
import com.cogniwide.TaskManager.DTO.UpdateTaskDTO;
import com.cogniwide.TaskManager.ServerResponse;
import com.cogniwide.TaskManager.customException.CustomException;
import com.cogniwide.TaskManager.entity.Task;
import com.cogniwide.TaskManager.service.taskService.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo/user/tasks/")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("task")
    public ResponseEntity<ServerResponse<?>> fetchTasksByUser() {
        try {
            List<Task> userTasks = taskService.fetchAllTaskByUser();
            ServerResponse<List<Task>> response = new ServerResponse<>(
                    HttpStatus.OK.value(), userTasks, "Fetched user's all tasks successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch tasks");
        }
    }

    @GetMapping("task/{taskId}")
    public ResponseEntity<ServerResponse<?>> fetchSpecificTask(@PathVariable int taskId) {
        try {
            Task task = taskService.fetchTaskById(taskId);
            ServerResponse<Task> response = new ServerResponse<>(
                    HttpStatus.OK.value(), task, "Fetched task ID: " + taskId + " from user's tasks");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch the task");
        }
    }

    @PostMapping("task")
    public ResponseEntity<ServerResponse<?>> addTask(@RequestBody AddTaskDTO newTask) {
        try {
            taskService.addTask(newTask);
            ServerResponse<AddTaskDTO> response = new ServerResponse<>(
                    HttpStatus.CREATED.value(), newTask, "User task created successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create task");
        }
    }

    @PutMapping("task/{taskId}")
    public ResponseEntity<ServerResponse<String>> updateTask(@PathVariable Integer taskId,
                                                        @RequestBody UpdateTaskDTO updateTaskDTO) {
        try {
            taskService.updateTask(taskId, updateTaskDTO);
            ServerResponse<String> response = new ServerResponse<>(
                    HttpStatus.OK.value(), "Task updated", "Task updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update task");
        }
    }

    @DeleteMapping("task/{taskId}")
    public ResponseEntity<ServerResponse<?>> deleteTask(@PathVariable Integer taskId) {
        try {
            taskService.deleteTaskById(taskId);
            ServerResponse<String> response = new ServerResponse<>(
                    HttpStatus.OK.value(), "Task " + taskId, "Task deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete task");
        }
    }
}
