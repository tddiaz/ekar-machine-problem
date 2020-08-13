package com.github.tddiaz.ekarmachineproblem.models;

import lombok.Getter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
@Getter
public abstract class Task {

    @Id
    protected String id;
    protected String requestId;
    protected Integer updatedCounterValue;
    protected boolean executed = false;

    protected Task() { }

    protected Task(String requestId) {
        this.id = UUID.randomUUID().toString();
        this.requestId = requestId;
    }
}
