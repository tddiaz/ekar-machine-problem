package com.github.tddiaz.ekarmachineproblem.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CounterUpdateLog {

    @Id
    private String id;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    private LocalDateTime timestamp;
    private String taskId;

    private CounterUpdateLog(String taskId, Status status) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.taskId = taskId;
    }

    public static CounterUpdateLog reachedMaxValue(String taskId) {
        return new CounterUpdateLog(taskId, Status.REACHED_MAX);
    }

    public static CounterUpdateLog reachedMinValue(String taskId) {
        return new CounterUpdateLog(taskId, Status.REACHED_MIN);
    }

    public enum Status {
        REACHED_MAX, REACHED_MIN
    }
}
