package com.github.tddiaz.ekarmachineproblem.controllers;

import com.github.tddiaz.ekarmachineproblem.controllers.dtos.request.IncreaseThreadsRequest;
import com.github.tddiaz.ekarmachineproblem.controllers.dtos.request.UpdateCounterValueRequest;
import com.github.tddiaz.ekarmachineproblem.controllers.dtos.response.GetRequestLogResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class AppControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    void givenRequest_whenIncreaseThreads_thenReturnAcceptedResponse() {

        var increaseThreadsRequest = new IncreaseThreadsRequest();
        increaseThreadsRequest.setProducerCount(10);
        increaseThreadsRequest.setConsumerCount(5);

        var requestId = given().header("Content-Type", "application/json").body(increaseThreadsRequest)
                        .when().post("/increaseThreads")
                        .then().statusCode(202).body("$", notNullValue()).extract().path("requestId");

        // verify request log
        var requestLog = given().header("Content-Type", "application/json")
                        .when().get("/requestLogs/{requestId}", requestId)
                        .then().statusCode(200).extract().as(GetRequestLogResponse.class);

        assertThat(requestLog).isNotNull();
        assertThat(requestLog.getConsumerCount()).isEqualTo(5);
        assertThat(requestLog.getProducerCount()).isEqualTo(10);
        assertThat(requestLog.getProducers()).isNotEmpty();
        assertThat(requestLog.getConsumers()).isNotEmpty();

        // verify counter value
        var counter = given().header("Content-Type", "application/json")
                .when().get("/counter")
                .then().extract().path("value");
        assertThat(counter).isEqualTo(55);
    }

    @Test
    void givenCounterValue_whenUpdateCounter_thenReturnSuccessResponse() {

        var counterUpdateRequest = new UpdateCounterValueRequest();
        counterUpdateRequest.setValue(51);

        given().header("Content-Type", "application/json").body(counterUpdateRequest)
        .when().put("/updateCounterValue")
        .then().statusCode(200);

        // verify counter value
        var counter = given().header("Content-Type", "application/json")
                .when().get("/counter")
                .then().extract().path("value");
        assertThat(counter).isEqualTo(51);
    }

    @Test
    void givenInvalidCounterValue_whenUpdateCounter_shouldReturnBadRequest() {

        {
            var counterUpdateRequest = new UpdateCounterValueRequest();
            counterUpdateRequest.setValue(-1);

            given().header("Content-Type", "application/json").body(counterUpdateRequest)
                    .when().put("/updateCounterValue")
                    .then().statusCode(400).body("message", notNullValue());
        }

        {
            var counterUpdateRequest = new UpdateCounterValueRequest();
            counterUpdateRequest.setValue(101);

            given().header("Content-Type", "application/json").body(counterUpdateRequest)
                    .when().put("/updateCounterValue")
                    .then().statusCode(400).body("message", notNullValue());
        }
    }
}