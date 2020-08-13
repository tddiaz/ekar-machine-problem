package com.github.tddiaz.ekarmachineproblem.models;

import com.github.tddiaz.ekarmachineproblem.services.Counter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@Entity
public class Producer extends Task {

    private Producer(String requestId) {
        super(requestId);
    }

    public static List<Producer> createMultiple(String requestId, int count) {
        return IntStream.range(0, count).mapToObj(i -> new Producer(requestId)).collect(Collectors.toList());
    }

    public void increaseCounter(Counter counter) {
        counter.increment();
        this.updatedCounterValue = counter.getValue();
        this.executed = true;
    }
}
