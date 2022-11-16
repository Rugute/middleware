package com.middleware.repositories;

import com.middleware.model.Years;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YearsRepository extends JpaRepository<Years, Long> {
     List<Years> findAll();

}