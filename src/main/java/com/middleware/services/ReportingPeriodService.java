package com.middleware.services;

import com.middleware.model.ReportingPeriod;
import com.middleware.repositories.ReportingPeriodRepository;
import com.middleware.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("reportingPeriodService")
public class ReportingPeriodService {

    private UserRepository userRepository;
    private ReportingPeriodRepository reportingPeriodRepository;

    Date nowDate = new Date();
    @Autowired
    public ReportingPeriodService(UserRepository userRepository,
                                  ReportingPeriodRepository reportingPeriodRepository
                       ) {
        this.userRepository = userRepository;
        this.reportingPeriodRepository = reportingPeriodRepository;
    }
    public List<ReportingPeriod> getAll(){return  reportingPeriodRepository.findAll();}
    public List<ReportingPeriod> getAllCurrentAndPast(int id ){return  reportingPeriodRepository.findByIdLessThanEqualOrderByStartdateDesc(id);}


    public ReportingPeriod getByQuarter(String q){
        return  reportingPeriodRepository.findByQuarter(q);
    }
    public List<ReportingPeriod> getByStatus(int status){
        return  reportingPeriodRepository.getByStatus(status);
    }

    public ReportingPeriod save(ReportingPeriod reportingPeriod) {
        return reportingPeriodRepository.save(reportingPeriod);
    }
}
