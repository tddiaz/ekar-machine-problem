package com.github.tddiaz.ekarmachineproblem.repositories;

import com.github.tddiaz.ekarmachineproblem.models.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepository extends JpaRepository<Consumer, String> {
}
