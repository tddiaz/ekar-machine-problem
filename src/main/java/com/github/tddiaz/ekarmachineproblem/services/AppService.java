package com.github.tddiaz.ekarmachineproblem.services;

import com.github.tddiaz.ekarmachineproblem.controllers.dtos.request.IncreaseThreadsRequest;
import com.github.tddiaz.ekarmachineproblem.controllers.dtos.request.UpdateCounterValueRequest;
import com.github.tddiaz.ekarmachineproblem.models.Consumer;
import com.github.tddiaz.ekarmachineproblem.models.Producer;
import com.github.tddiaz.ekarmachineproblem.models.RequestLog;
import com.github.tddiaz.ekarmachineproblem.repositories.RequestLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppService {

    private final RequestLogRepository requestLogRepository;
    private final ProducerTaskHandler producerTaskHandler;
    private final ConsumerTaskHandler consumerTaskHandler;
    private final ExecutorService executorService;
    private final Counter counter;

    public String increaseThreads(IncreaseThreadsRequest request) {

        var requestLog = requestLogRepository.save(RequestLog.create(request.getProducerCount(), request.getConsumerCount()));
        LOGGER.info("Received increase threads request. \n id = {} \n producerCount = {} \n consumerCount = {}",
                requestLog.getId(), requestLog.getProducerCount(), requestLog.getConsumerCount());

        var producers = Producer.createMultiple(requestLog.getId(), requestLog.getProducerCount());
        var consumers = Consumer.createMultiple(requestLog.getId(), requestLog.getConsumerCount());

        producers.forEach(producer -> executorService.submit(() -> producerTaskHandler.handle(producer)));
        consumers.forEach(consumer -> executorService.submit(() -> consumerTaskHandler.handle(consumer)));

        return requestLog.getId();
    }

    public void updateCounterValue(UpdateCounterValueRequest request) {
        counter.lock();
        try {
            counter.setValue(request.getValue());
            LOGGER.info("Counter updated value: {}", counter.getValue());
        } finally {
            counter.unlock();
        }
    }

    public RequestLog findRequestLogById(String requestId) {
        return requestLogRepository.findById(requestId).orElse(null);
    }

    public Integer getCounterValue() {
        return counter.getValue();
    }

}
