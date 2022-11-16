package com.middleware.repositories;

import com.middleware.model.ReportingPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportingPeriodRepository extends JpaRepository<ReportingPeriod, Long> {
    List<ReportingPeriod> findAllByOrderByIdDesc();
    List<ReportingPeriod> findByIdLessThanEqualOrderByStartdateDesc(int id);
    ReportingPeriod findByQuarter(String qid);
    List<ReportingPeriod> getByStatus(Integer status);
    ReportingPeriod findByMonthAndYear(String month,String year);
}
