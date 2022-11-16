package ke.or.karp.middleware.services;

import ke.or.karp.middleware.model.Dataset;
import ke.or.karp.middleware.model.Period;
import ke.or.karp.middleware.model.Years;
import ke.or.karp.middleware.repositories.DatasetRepository;
import ke.or.karp.middleware.repositories.PeriodRepository;
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