package com.middleware.repositories;

import com.middleware.model.Alldata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlldataRepository extends JpaRepository<Alldata, Long> {
    List<Alldata> findAll();
    Alldata findByMid(int id);
    List<Alldata> findByMidAndOrgunitAndDatasetAndPeriod(int mid,String org,String dataset,String period);
    //List<Alldata> findByStatus(String status);
}

