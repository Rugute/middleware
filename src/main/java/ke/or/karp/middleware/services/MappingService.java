package ke.or.karp.middleware.services;

import ke.or.karp.middleware.model.Dataset;
import ke.or.karp.middleware.model.Mapping;
import ke.or.karp.middleware.repositories.DatasetRepository;
import ke.or.karp.middleware.repositories.MappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("mappingService")
public class MappingService {
    Date nowDate = new Date();
    private MappingRepository mappingRepository;
    @Autowired
    public MappingService(MappingRepository mappingRepository) {
        this.mappingRepository = mappingRepository;
    }
    public Mapping save(Mapping mapping) {
        return mappingRepository.save(mapping);
    }
    public void deleteMapping(Mapping mapping){
        mappingRepository.delete(mapping);
    }
    public List<Mapping> getAllMappingt(){return  mappingRepository.findAll();}
    public List<Mapping> getByID(String id){
        return  mappingRepository.findByDATAELEMENTSPECIF(id);
    }
    public List<Mapping> getBycategorycombospecificID(String id){
        return  mappingRepository.findByCATEGORYCOMBOSPECIFIC(id);
    }
    public List<Mapping> getBycategorycombospecificIDAndDataelementLike(String combo, String dataelement){
        return  mappingRepository.findByCATEGORYCOMBOSPECIFICContainingAndDATAELEMENTSPECIFContaining(combo,dataelement);
    }
    public List<Mapping> getBycategorycombospecificIDAndDataelement(String combo, String dataelement){
        return  mappingRepository.findByCATEGORYCOMBOSPECIFICAndDATAELEMENTSPECIF(combo,dataelement);
    }
    public List<Mapping> getByAgeGendercategorycombospecificIDAndDataelement(String year,String gender,String combo, String dataelement){
        return  mappingRepository.findByCATEGORYCOMBOYEARAndCATEGORYCOMBOGENDERAndCATEGORYCOMBOSPECIFICAndDATAELEMENTSPECIF(year,gender,combo,dataelement);
    }
    public List<Mapping> findByDENMAPPER(String dataelement){
        return  mappingRepository.findByDENMAPPER(dataelement);
    }
    public List<Mapping> findByComboYearandDENMAPPER(String combo,String year,String dataelement){
        return  mappingRepository.findByCATEGORYCOMBOSPECIFICAndCATEGORYCOMBOYEARAndDENMAPPER(combo,year,dataelement);
    }
    public List<Mapping> findByCATEGORYCOMBOSPECIFICAndDENMAPPER(String combo,String dataelement){
        return  mappingRepository.findByCATEGORYCOMBOSPECIFICAndDENMAPPER(combo,dataelement);
    }



}
