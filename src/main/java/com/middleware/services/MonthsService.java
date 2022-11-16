package com.middleware.services;

import com.middleware.model.Months;
import com.middleware.repositories.MonthsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service("MonthsService")
public class MonthsService {
    Date nowDate = new Date();
    private MonthsRepository monthsRepository;
    @Autowired
    public MonthsService(MonthsRepository monthsRepository) {
        this.monthsRepository = monthsRepository;
    }
    public Months save(Months dataset) {
        return monthsRepository.save(dataset);
    }
    public void delete(Months dataset){
         monthsRepository.delete(dataset);
    }
    public List<Months> getAllDataset(){return  monthsRepository.findAll();}
    /*public Months getByID(String id){
        return  monthsRepository.findAll();
    }*/
}