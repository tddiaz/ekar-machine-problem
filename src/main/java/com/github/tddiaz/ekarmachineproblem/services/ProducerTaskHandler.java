package com.github.tddiaz.ekarmachineproblem.services;

import com.github.tddiaz.ekarmachineproblem.models.CounterUpdateLog;
import com.github.tddiaz.ekarmachineproblem.models.Producer;
import com.github.tddiaz.ekarmachineproblem.repositories.CounterUpdateLogRepository;
import com.github.tddiaz.ekarmachineproblem.repositories.ProducerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProducerTaskHandler {

    private final ProducerRepository producerRepository;
    private final CounterUpdateLogRepository counterUpdateLogRepository;
    private final Counter counter;

    public void handle(Producer producer) {
        counter.lock();

        try {
            if (counter.isValueReachedMax()) {
                LOGGER.info("Counter value reached maximum. Producer '{}' will not be executed.", producer.getId());
                producerRepository.save(producer);
                return;
            }

            producer.increaseCounter(counter);

            LOGGER.info("Producer '{}' successfully executed. Updated counter value: '{}'", producer.getId(), producer.getUpdatedCounterValue());
            producerRepository.save(producer);

            if (counter.isValueReachedMax()) {
                counterUpdateLogRepository.save(CounterUpdateLog.reachedMaxValue(producer.getId()));
            }

        } finally {
            counter.unlock();
        }
    }
}
