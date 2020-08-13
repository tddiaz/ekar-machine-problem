package com.github.tddiaz.ekarmachineproblem.repositories;

import com.github.tddiaz.ekarmachineproblem.models.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<Producer, String> {
}
