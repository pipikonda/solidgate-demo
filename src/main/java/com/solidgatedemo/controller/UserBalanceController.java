package com.solidgatedemo.controller;

import com.solidgatedemo.service.UserBalanceService;
import com.solidgatedemo.utils.Result;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserBalanceController {

    private final UserBalanceService userBalanceService;

    @PostMapping("/api/set-users-balacne")
    public Result<Integer> updateBalance(@RequestBody @Valid SetUserBalanceRequest request) {
        return new Result<>(userBalanceService.updateBalance1s(request.balances()));
    }

    public record SetUserBalanceRequest(Map<@NotNull Long, @NotNull Long> balances) {

    }
}
