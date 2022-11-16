package com.middleware.services;

import com.middleware.model.DatabaseInfo;
import com.middleware.repositories.DatabasesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service("DatabaseinfoService")
public class DatabaseinfoService {
    Date nowDate = new Date();
    private final Path root = Paths.get("uploads");
    private DatabasesRepository databasesRepository;
    @Autowired
    public DatabaseinfoService(DatabasesRepository databasesRepository) {
        this.databasesRepository = databasesRepository;
    }
    public DatabaseInfo save(DatabaseInfo dataset) {
        return databasesRepository.save(dataset);
    }
    public void delete(DatabaseInfo dataset){
        databasesRepository.delete(dataset);
    }
    public List<DatabaseInfo> getAllDataset(){return  databasesRepository.findAll();}
    public DatabaseInfo getByDbname(String dbname){
        return  databasesRepository.findByDbname(dbname);
    }

}