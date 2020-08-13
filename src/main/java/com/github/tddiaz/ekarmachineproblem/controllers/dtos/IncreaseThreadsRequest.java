package com.github.tddiaz.ekarmachineproblem.controllers.dtos;

import lombok.Data;

@Data
public class IncreaseThreadsRequest {
    private int producerCount;
    private int consumerCount;
}
