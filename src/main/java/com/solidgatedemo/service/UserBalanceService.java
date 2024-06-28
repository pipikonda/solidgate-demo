package com.solidgatedemo.service;

import com.solidgatedemo.repository.UserBalanceRepository;
import com.solidgatedemo.repository.domain.UserBalance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserBalanceService {

    private final UserBalanceRepository userBalanceRepository;

    @Transactional
    public Integer updateBalance1s(Map<Long, Long> userBalances) {
        List<UserBalance> newBalances = userBalances.entrySet()
                .stream()
                .flatMap(e -> userBalanceRepository.findById(e.getKey()).stream())
                .map(e -> e.toBuilder()
                        .balance(userBalances.get(e.getId()))
                        .build())
                .toList();
        userBalanceRepository.saveAll(newBalances);
        return newBalances.size();
    }

}
