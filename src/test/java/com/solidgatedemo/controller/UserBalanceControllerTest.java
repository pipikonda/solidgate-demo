package com.solidgatedemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solidgatedemo.TestContainerBaseClass;
import com.solidgatedemo.repository.UserBalanceRepository;
import com.solidgatedemo.repository.domain.UserBalance;
import com.solidgatedemo.utils.Result;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wiremock.com.google.common.collect.Iterators;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.apache.http.HttpStatus.SC_OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
class UserBalanceControllerTest extends TestContainerBaseClass {

    public static final ResponseSpecification OK_STATUS_CODE_AND_CONTENT_TYPE =
            new ResponseSpecBuilder()
                    .expectStatusCode(SC_OK)
                    .expectContentType(APPLICATION_JSON_VALUE)
                    .build();
    @Autowired
    private UserBalanceRepository userBalanceRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecureRandom secureRandom;

    @BeforeEach
    @AfterEach
    void clearDb() {
        userBalanceRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    void testUpdateBalances_shouldReturnZero_whenInputUsersAreNotPresent() {
        List<UserBalance> balances = LongStream.range(1L, 22)
                .boxed()
                .map(e -> UserBalance.builder()
                        .id(e)
                        .balance(secureRandom.nextLong(1, 8989))
                        .build())
                .toList();
        userBalanceRepository.saveAll(balances);
        Map<Long, Long> map = LongStream.range(55, 88)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), e -> secureRandom.nextLong(1, 4444)));

        String expectedResponse = objectMapper.writeValueAsString(new Result<>(0));
        RestAssured.given()
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(new UserBalanceController.SetUserBalanceRequest(map)))
                .post("/api/set-users-balacne")
                .then()
                .spec(OK_STATUS_CODE_AND_CONTENT_TYPE)
                .body(Matchers.equalTo(expectedResponse));
    }

    @Test
    @SneakyThrows
    void testUpdateBalances_shouldUpdateAllRows_whenInputUsersArePresent() {
        List<UserBalance> balances = LongStream.range(1, 22)
                .boxed()
                .map(e -> UserBalance.builder()
                        .id(e)
                        .balance(secureRandom.nextLong(1, 8989))
                        .build())
                .toList();
        userBalanceRepository.saveAll(balances);

        Map<Long, Long> map = LongStream.range(1, 22)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), e -> secureRandom.nextLong(1, 4444)));

        String expectedResponse = objectMapper.writeValueAsString(new Result<>(map.size()));
        RestAssured.given()
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(new UserBalanceController.SetUserBalanceRequest(map)))
                .post("/api/set-users-balacne")
                .then()
                .spec(OK_STATUS_CODE_AND_CONTENT_TYPE)
                .body(Matchers.equalTo(expectedResponse));
    }

    @Test
    @SneakyThrows
    void testUpdateBalances_shouldUpdateExistsRows_whenInputUsersArePartiallyPresent() {
        List<UserBalance> balances = LongStream.range(1, 22)
                .boxed()
                .map(e -> UserBalance.builder()
                        .id(e)
                        .balance(secureRandom.nextLong(1, 8989))
                        .build())
                .toList();
        userBalanceRepository.saveAll(balances);

        Map<Long, Long> map = LongStream.range(19, 67)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), e -> secureRandom.nextLong(1, 4444)));

        String expectedResponse = objectMapper.writeValueAsString(new Result<>(3));
        RestAssured.given()
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(new UserBalanceController.SetUserBalanceRequest(map)))
                .post("/api/set-users-balacne")
                .then()
                .spec(OK_STATUS_CODE_AND_CONTENT_TYPE)
                .body(Matchers.equalTo(expectedResponse));
    }

    @Test
    @SneakyThrows
    @Disabled
    void performanceTest() {
        List<UserBalance> balances = LongStream.range(1L, 300_000L)
                .boxed()
                .map(e -> UserBalance.builder()
                        .id(e)
                        .balance(secureRandom.nextLong(1, 8989))
                        .build())
                .toList();
        userBalanceRepository.saveAll(balances);
        log.info("Count ===> " + balances.size());

        Map<Long, Long> map = LongStream.range(1L, 1_000_000L)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), e -> secureRandom.nextLong(1, 4444)));
        log.info("size is {}", map.size());

        long start = System.currentTimeMillis();
        RestAssured.given()
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(new UserBalanceController.SetUserBalanceRequest(map)))
                .post("/api/set-users-balacne")
                .then()
                .spec(OK_STATUS_CODE_AND_CONTENT_TYPE);

        long end = System.currentTimeMillis();
        log.info("Exec time " + (end - start));

        int size = Iterators.size(userBalanceRepository.findAll().iterator());
        log.info("count is " + size);
    }
}