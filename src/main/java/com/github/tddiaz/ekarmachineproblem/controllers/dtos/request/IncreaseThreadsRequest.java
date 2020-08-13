package com.github.tddiaz.ekarmachineproblem.controllers.dtos.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IncreaseThreadsRequest {
    private int producerCount;
    private int consumerCount;
}
