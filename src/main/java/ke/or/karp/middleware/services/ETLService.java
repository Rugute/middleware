package ke.or.karp.middleware.services;

import ke.or.karp.middleware.model.ETL;
import ke.or.karp.middleware.model.Period;
import ke.or.karp.middleware.repositories.ETLRepository;
import ke.or.karp.middleware.repositories.PeriodRepository;
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
