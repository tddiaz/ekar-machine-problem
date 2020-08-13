package com.github.tddiaz.ekarmachineproblem.controllers;

import com.github.tddiaz.ekarmachineproblem.controllers.dtos.request.IncreaseThreadsRequest;
import com.github.tddiaz.ekarmachineproblem.controllers.dtos.request.UpdateCounterValueRequest;
import com.github.tddiaz.ekarmachineproblem.controllers.dtos.response.ErrorResponse;
import com.github.tddiaz.ekarmachineproblem.controllers.dtos.response.GetCounterValueResponse;
import com.github.tddiaz.ekarmachineproblem.controllers.dtos.response.GetRequestLogResponse;
import com.github.tddiaz.ekarmachineproblem.controllers.dtos.response.IncreaseThreadsResponse;
import com.github.tddiaz.ekarmachineproblem.services.AppService;
import com.github.tddiaz.ekarmachineproblem.services.InvalidCounterValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public ResponseEntity<GetRequestLogResponse> findRequestLog(@PathVariable("id") String requestId) {
        return ResponseEntity.ok(GetRequestLogResponse.createFrom(appService.findRequestLogById(requestId)));
    }

    @GetMapping("/counter")
    public ResponseEntity<GetCounterValueResponse> getCounterValue() {
        return ResponseEntity.ok(new GetCounterValueResponse(appService.getCounterValue()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(InvalidCounterValueException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
}
