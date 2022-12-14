package com.middleware.repositories;

import com.middleware.model.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DatasetRepository extends JpaRepository<Dataset, Long> {
    List<Dataset> findAll();
    Dataset findByDisplayid(String qid);
    List<Dataset> findByStatus(String status);

}

