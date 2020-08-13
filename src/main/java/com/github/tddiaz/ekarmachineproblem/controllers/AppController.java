package com.github.tddiaz.ekarmachineproblem.controllers;

import com.github.tddiaz.ekarmachineproblem.controllers.dtos.CounterValueResponse;
import com.github.tddiaz.ekarmachineproblem.controllers.dtos.IncreaseThreadsRequest;
import com.github.tddiaz.ekarmachineproblem.controllers.dtos.IncreaseThreadsResponse;
import com.github.tddiaz.ekarmachineproblem.controllers.dtos.UpdateCounterValueRequest;
import com.github.tddiaz.ekarmachineproblem.models.RequestLog;
import com.github.tddiaz.ekarmachineproblem.services.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppController {

    private final AppService appService;

    @PostMapping("/increaseThreads")
    public ResponseEntity<IncreaseThreadsResponse> increaseThreads(@RequestBody IncreaseThreadsRequest request) {
        return ResponseEntity.accepted().body(new IncreaseThreadsResponse(appService.increaseThreads(request)));
    }

    @PutMapping("/updateCounterValue")
    @ResponseStatus(HttpStatus.OK)
    public void updateCounterValue(@RequestBody UpdateCounterValueRequest request) {
        appService.updateCounterValue(request);
    }

    @GetMapping("/requestLogs/{id}")
    public ResponseEntity<RequestLog> findRequestLog(@PathVariable("id") String requestId) {
        return ResponseEntity.ok(appService.findRequestLogById(requestId));
    }

    @GetMapping("/counter")
    public ResponseEntity<CounterValueResponse> getCounterValue() {
        return ResponseEntity.ok(new CounterValueResponse(appService.getCounterValue()));
    }
}
