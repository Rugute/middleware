package ke.or.karp.middleware.services;

import ke.or.karp.middleware.model.Dataset;
import ke.or.karp.middleware.repositories.DatasetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("datasetService")
public class DatasetService {
    Date nowDate = new Date();
    private DatasetRepository datasetRepository;
    @Autowired
    public DatasetService(DatasetRepository datasetRepository) {
        this.datasetRepository = datasetRepository;
    }
    public Dataset save(Dataset dataset) {
        return datasetRepository.save(dataset);
    }
    public void deleteBank(Dataset dataset){
        datasetRepository.delete(dataset);
    }
    public List<Dataset> getAllDataset(){return  datasetRepository.findAll();}
    public Dataset getByID(String id){
        return  datasetRepository.findByDisplayid(id);
    }
    public List<Dataset> getAllDatasetBystatus(String status){return  datasetRepository.findByStatus(status);}
   }