package com.middleware.services;

import com.middleware.repositories.PeriodRepository;
import com.middleware.model.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("PeriodService")
public class PeriodService {
    Date nowDate = new Date();
    private PeriodRepository periodRepository;
    @Autowired
    public PeriodService(PeriodRepository periodRepository) {
        this.periodRepository = periodRepository;
    }
    public Period save(Period dataset) {
        return periodRepository.save(dataset);
    }
    public void deleteBank(Period dataset){
        periodRepository.delete(dataset);
    }
    public List<Period> getAllDataset(){return  periodRepository.findAllByOrderByIdDesc();}
    public Period getByID(String id){
        return  periodRepository.findByDisplayid(id);
    }
}