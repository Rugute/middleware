package com.middleware.services;

import com.middleware.model.ETL;
import com.middleware.repositories.ETLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("ETLService")
public class ETLService {
    Date nowDate = new Date();
    private ETLRepository etlRepository;
    @Autowired
    public ETLService(ETLRepository etlRepository) {
        this.etlRepository = etlRepository;
    }

    public ETL save(ETL etl) {
        return etlRepository.save(etl);
    }
    public void delete(ETL dataset){
        etlRepository.delete(dataset);
    }
    public List<ETL> getAllDataset(){return  etlRepository.findAll();}


}
