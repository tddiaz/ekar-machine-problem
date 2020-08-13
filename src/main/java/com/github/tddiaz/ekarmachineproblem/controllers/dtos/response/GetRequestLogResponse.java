package com.github.tddiaz.ekarmachineproblem.controllers.dtos.response;

import com.github.tddiaz.ekarmachineproblem.models.RequestLog;
import com.github.tddiaz.ekarmachineproblem.models.Task;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GetRequestLogResponse {

    private String requestId;
    private Integer producerCount;
    private Integer consumerCount;
    private LocalDateTime timestamp;
    private List<TaskDto> producers;
    private List<TaskDto> consumers;

    public static GetRequestLogResponse createFrom(RequestLog requestLog) {
        var response = new GetRequestLogResponse();
        response.requestId = requestLog.getId();
        response.timestamp = requestLog.getTimestamp();
        response.producerCount = requestLog.getProducerCount();
        response.consumerCount = requestLog.getConsumerCount();
        response.producers = requestLog.getProducers().stream().map(TaskDto::createFrom).collect(Collectors.toList());
        response.consumers = requestLog.getConsumers().stream().map(TaskDto::createFrom).collect(Collectors.toList());

        return response;
    }

    @Data
    public static class TaskDto {
        protected String id;
        protected Integer updatedCounterValue;
        protected boolean executed;

        public static <T extends Task >TaskDto createFrom(T task) {
            var response = new TaskDto();
            response.id = task.getId();
            response.updatedCounterValue = task.getUpdatedCounterValue();
            response.executed = task.isExecuted();

            return response;
        }
    }
}
