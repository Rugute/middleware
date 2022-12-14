package com.middleware.services;

import com.middleware.model.Alldata;
import com.middleware.repositories.AlldataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("alldataService")
public class AlldataService {
    Date nowDate = new Date();
    private AlldataRepository alldataRepository;
    @Autowired
    public AlldataService(AlldataRepository alldataRepository) {
        this.alldataRepository = alldataRepository;
    }
    public Alldata save(Alldata dataset) {
        return alldataRepository.save(dataset);
    }
    public void deleteBank(Alldata dataset){
        alldataRepository.delete(dataset);
    }
    public List<Alldata> getAllData(){return  alldataRepository.findAll();}
    public List<Alldata> getSpecificAllData(int mid,String org,String dataset,String period){
        return  alldataRepository.findByMidAndOrgunitAndDatasetAndPeriod(mid,org,dataset,period);}
    /*public Alldata getByID(String id){
        return  alldataRepository.findByDisplayid(id);

    }*/
    //public List<Alldata> getAllDatasetByStatus(String status){return  alldataRepository.findByStatus(status);}

}
