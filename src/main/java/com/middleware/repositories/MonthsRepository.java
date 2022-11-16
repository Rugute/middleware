package com.middleware.repositories;

import com.middleware.model.Months;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonthsRepository extends JpaRepository<Months, Long> {
    List<Months> findAll();

}