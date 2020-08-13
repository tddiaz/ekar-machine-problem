package com.github.tddiaz.ekarmachineproblem.services;

import com.github.tddiaz.ekarmachineproblem.models.Consumer;
import com.github.tddiaz.ekarmachineproblem.models.CounterUpdateLog;
import com.github.tddiaz.ekarmachineproblem.repositories.ConsumerRepository;
import com.github.tddiaz.ekarmachineproblem.repositories.CounterUpdateLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConsumerTaskHandler {

    private final ConsumerRepository consumerRepository;
    private final CounterUpdateLogRepository counterUpdateLogRepository;
    private final Counter counter;

    public void handle(Consumer consumer) {
        counter.lock();

        try {
            if (counter.isValueReachedMin()) {
                LOGGER.info("Counter value reached minimum. Consumer '{}' will not be executed.", consumer.getId());
                consumerRepository.save(consumer);
                return;
            }

            consumer.decreaseCounter(counter);

            LOGGER.info("Consumer '{}' successfully executed. Updated counter value: '{}'", consumer.getId(), consumer.getUpdatedCounterValue());
            consumerRepository.save(consumer);

            if (counter.isValueReachedMin()) {
                counterUpdateLogRepository.save(CounterUpdateLog.reachedMinValue(consumer.getId()));
            }
        } finally {
            counter.unlock();
        }
    }

}
