package com.github.tddiaz.ekarmachineproblem.repositories;

import com.github.tddiaz.ekarmachineproblem.models.CounterUpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterUpdateLogRepository extends JpaRepository<CounterUpdateLog, String> {

}
