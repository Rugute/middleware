package ke.or.karp.middleware.services;

import ke.or.karp.middleware.model.DatabaseInfo;
import ke.or.karp.middleware.model.Months;
import ke.or.karp.middleware.repositories.DatabasesRepository;
import ke.or.karp.middleware.repositories.MonthsRepository;
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