package com.cogniwide.TaskManager.scheduler;

import com.cogniwide.TaskManager.entity.Task;
import com.cogniwide.TaskManager.enums.Status;
import com.cogniwide.TaskManager.repositopry.taskRepository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class TaskStatusScheduler {

    @Autowired
    private TaskRepository taskRepository;

    @Scheduled(fixedRate = 60 * 60 * 1000) // Runs every hour
    @Transactional
    public void markMissedTasks() {
        LocalDate today = LocalDate.now();

        // Fetch all tasks which are not COMPLETED
        List<Task> tasks = taskRepository.findByStatusNot(Status.COMPLETED);

        for (Task task : tasks) {
            // dueDate is LocalDate now, so direct comparison
            if (task.getDueDate().isBefore(today)) {
                task.setStatus(Status.MISSED);
            }
        }

        taskRepository.saveAll(tasks);
    }
}
