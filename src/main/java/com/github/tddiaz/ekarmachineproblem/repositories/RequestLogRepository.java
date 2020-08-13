package com.github.tddiaz.ekarmachineproblem.repositories;

import com.github.tddiaz.ekarmachineproblem.models.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestLogRepository extends JpaRepository<RequestLog, String> {
}
