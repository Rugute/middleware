package com.middleware.repositories;

import com.middleware.model.DataElementGroups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataElementGroupsRepository extends JpaRepository<DataElementGroups, Long> {
    List<DataElementGroups> findAll();
    DataElementGroups findByDisplayid(String qid);
    List<DataElementGroups> findByStatus(String status);
}

