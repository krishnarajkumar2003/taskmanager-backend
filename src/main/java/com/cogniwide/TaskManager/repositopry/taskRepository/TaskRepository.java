package com.cogniwide.TaskManager.repositopry.taskRepository;

import com.cogniwide.TaskManager.entity.Task;
import com.cogniwide.TaskManager.entity.UserEntity;
import com.cogniwide.TaskManager.enums.Status;
import com.cogniwide.TaskManager.enums.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Integer> {
    List<Task> findByUser(UserEntity user);

    List<Task> findByStatusNot(Status status);

    List<Task> findByUserAndStatus(UserEntity user, Status status);

    List<Task> findByUserAndPriority(UserEntity user, TaskPriority priority);

    List<Task> findByUserAndStatusAndPriority(UserEntity user, Status status, TaskPriority priority);

}