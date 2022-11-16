package com.middleware.services;

import com.middleware.model.DataElementGroups;
import com.middleware.repositories.DataElementGroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("datasetelementgroupsService")
public class DataElementGroupsService {
    Date nowDate = new Date();
    private DataElementGroupsRepository dataElementGroupsRepository;
    @Autowired
    public DataElementGroupsService(DataElementGroupsRepository dataElementGroupsRepository) {
        this.dataElementGroupsRepository = dataElementGroupsRepository;
    }
    public DataElementGroups save(DataElementGroups dataset) {
        return dataElementGroupsRepository.save(dataset);
    }
    public void deleteBank(DataElementGroups dataset){
        dataElementGroupsRepository.delete(dataset);
    }
    public List<DataElementGroups> getAllDataset(){return  dataElementGroupsRepository.findAll();}
    public DataElementGroups getByID(String id){
        return  dataElementGroupsRepository.findByDisplayid(id);
    }
    public List<DataElementGroups> getAllDatasetByStatus(String status){return  dataElementGroupsRepository.findByStatus(status);}

}
