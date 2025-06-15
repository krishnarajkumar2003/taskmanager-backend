package com.cogniwide.TaskManager.entity;

import com.cogniwide.TaskManager.enums.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "task name",nullable = false)
    private String title;

    @Column(name = "due date",updatable = false,nullable = false)
    private LocalDate dueDate;

    @Column(name = "priority",nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Column(name = "category",nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id",updatable = false,nullable = false)
    @JsonIgnore
    private UserEntity user;
}
