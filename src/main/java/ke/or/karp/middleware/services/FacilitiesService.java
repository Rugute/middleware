package ke.or.karp.middleware.services;

import ke.or.karp.middleware.model.Dataset;
import ke.or.karp.middleware.model.Facilities;
import ke.or.karp.middleware.repositories.DatasetRepository;
import ke.or.karp.middleware.repositories.FacilitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("facilitiesService")
public class FacilitiesService {
    Date nowDate = new Date();
    private FacilitiesRepository facilitiesRepository;
    @Autowired
    public FacilitiesService(FacilitiesRepository facilitiesRepository) {
        this.facilitiesRepository = facilitiesRepository;
    }
    public Facilities save(Facilities dataset) {
        return facilitiesRepository.save(dataset);
    }
    public void deleteBank(Facilities dataset){
        facilitiesRepository.delete(dataset);
    }
    public List<Facilities> getAllDataset(){return  facilitiesRepository.findAll();}
    public Facilities getByMFLCODE(double id){
        return  facilitiesRepository.findByMflcode(id);
    }
    public Facilities getByName(String id){
        return  facilitiesRepository.findByFacilityname(id);
    }
    public Facilities getByNameLike(String id){
        return  facilitiesRepository.findByFacilitynameContaining(id);
    }

    public Facilities getByFacilityNameLike(String id){
        return  facilitiesRepository.findByFacilitynameLike(id);
    }
    public Facilities getByFacilityNameEndwith(String id){
        return  facilitiesRepository.findByFacilitynameEndsWith(id);
    }
    public List<Facilities> searchByFnameLike(String id){
        return  facilitiesRepository.searchByFnameLike(id);
    }
    public List<Facilities> searchByFtypeLike(String id){
        return  facilitiesRepository.searchByFtypeLike(id);
    }




}
