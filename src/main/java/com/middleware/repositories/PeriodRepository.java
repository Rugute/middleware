package com.middleware.repositories;

import com.middleware.model.Period;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeriodRepository extends JpaRepository<Period, Long> {
    List<Period> findAllByOrderByIdDesc();
    Period findByDisplayid(String qid);
   }

