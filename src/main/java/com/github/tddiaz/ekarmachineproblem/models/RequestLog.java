package com.github.tddiaz.ekarmachineproblem.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Entity
public class RequestLog {

    @Id
    private final String id;
    private final Integer producerCount;
    private final Integer consumerCount;
    private final LocalDateTime timestamp;

    @OneToMany(mappedBy = "requestId")
    private List<Consumer> consumers;

    @OneToMany(mappedBy = "requestId")
    private List<Producer> producers;


    public static RequestLog create(Integer producerCount, Integer consumerCount) {
       return new RequestLog(UUID.randomUUID().toString(), producerCount, consumerCount, LocalDateTime.now());
    }
}
