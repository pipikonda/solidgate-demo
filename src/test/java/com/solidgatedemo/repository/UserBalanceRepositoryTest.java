package com.solidgatedemo.repository;

import com.solidgatedemo.TestContainerBaseClass;
import com.solidgatedemo.repository.domain.UserBalance;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class UserBalanceRepositoryTest extends TestContainerBaseClass {

    @Autowired
    private UserBalanceRepository instance;

    @BeforeEach
    @AfterEach
    void clearDb() {
        instance.deleteAll();
    }

    @Test
    void testSave() {
        UserBalance saved = instance.save(UserBalance.builder()
                .id(2L)
                .name("Some name")
                .build());

        assertThat(saved)
                .isEqualTo(instance.findById(2L)
                .orElseThrow());
    }

}