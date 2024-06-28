package com.solidgatedemo.repository;

import com.solidgatedemo.repository.domain.UserBalance;
import org.springframework.data.repository.CrudRepository;

public interface UserBalanceRepository extends CrudRepository<UserBalance, Long> {


}
