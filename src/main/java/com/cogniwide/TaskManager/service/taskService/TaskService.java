package com.cogniwide.TaskManager.service.taskService;

import com.cogniwide.TaskManager.DTO.AddTaskDTO;
import com.cogniwide.TaskManager.DTO.UpdateTaskDTO;
import com.cogniwide.TaskManager.customException.CustomException;
import com.cogniwide.TaskManager.entity.Task;
import com.cogniwide.TaskManager.entity.UserEntity;
import com.cogniwide.TaskManager.enums.Status;
import com.cogniwide.TaskManager.enums.TaskPriority;
import com.cogniwide.TaskManager.repositopry.taskRepository.TaskRepository;
import com.cogniwide.TaskManager.repositopry.userRepository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity getAuthenticatedUser() {
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            return userRepository.findByUsername(userName)
                    .orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "User not authorized"));
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch authenticated user");
        }
    }


    public void addTask(AddTaskDTO addTaskDTO) {
        try {
            UserEntity user = getAuthenticatedUser();

            if (addTaskDTO.getTitle() == null || addTaskDTO.getTitle().isEmpty() ||
                    addTaskDTO.getCategory() == null || addTaskDTO.getPriority() == null) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Missing title, category, or priority");
            }

            Task newTask = new Task();
            newTask.setTitle(addTaskDTO.getTitle());
            newTask.setCategory(addTaskDTO.getCategory());
            newTask.setPriority(addTaskDTO.getPriority());
            newTask.setStatus(Status.PENDING);
            newTask.setDueDate(LocalDate.now());
            newTask.setUser(user);

            taskRepository.save(newTask);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while adding task");
        }
    }

    public void updateTask(Integer taskId, UpdateTaskDTO updateTaskDTO) {
        try {
            UserEntity user = getAuthenticatedUser();

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Task not found: " + taskId));

            if (!task.getUser().getId().equals(user.getId())) {
                throw new CustomException(HttpStatus.FORBIDDEN, "You are not allowed to update this task");
            }

            if (updateTaskDTO.getTitle() != null) task.setTitle(updateTaskDTO.getTitle());
            if (updateTaskDTO.getPriority() != null) task.setPriority(updateTaskDTO.getPriority());
            if (updateTaskDTO.getCategory() != null) task.setCategory(updateTaskDTO.getCategory());
            if (updateTaskDTO.getStatus() != null) task.setStatus(updateTaskDTO.getStatus());

            taskRepository.save(task);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while updating task");
        }
    }

    public void deleteTaskById(Integer taskId) {
        try {
            UserEntity user = getAuthenticatedUser();

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Task not found: " + taskId));

            if (!task.getUser().getId().equals(user.getId())) {
                throw new CustomException(HttpStatus.FORBIDDEN, "You are not authorized to delete this task");
            }

            taskRepository.deleteById(taskId);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while deleting task");
        }
    }

    public List<Task> fetchAllTaskByUser() {
        try {
            UserEntity user = getAuthenticatedUser();
            return taskRepository.findByUser(user);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while fetching tasks");
        }
    }

    public Task fetchTaskById(int taskId) {
        try {
            UserEntity user = getAuthenticatedUser();

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Task not found: " + taskId));

            if (!task.getUser().getId().equals(user.getId())) {
                throw new CustomException(HttpStatus.FORBIDDEN, "You are not authorized to view this task");
            }

            return task;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while fetching task by ID");
        }
    }

    public List<Task> getFilteredTasks(Status status, TaskPriority priority) {
        try {
            UserEntity user = getAuthenticatedUser();

            if (status != null && priority != null) {
                return taskRepository.findByUserAndStatusAndPriority(user, status, priority);
            } else if (status != null) {
                return taskRepository.findByUserAndStatus(user, status);
            } else if (priority != null) {
                return taskRepository.findByUserAndPriority(user, priority);
            } else {
                return taskRepository.findByUser(user);
            }

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while filtering tasks");
        }
    }
}
