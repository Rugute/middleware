package com.middleware.repositories;

import com.middleware.model.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MappingRepository extends JpaRepository<Mapping, Long> {
    List<Mapping> findAll();
    List<Mapping> findByDATAELEMENTSPECIF(String qid);
    List<Mapping> findByCATEGORYCOMBOSPECIFIC(String qid);
    List<Mapping> findByCATEGORYCOMBOSPECIFICContainingAndDATAELEMENTSPECIFContaining(String combo,String dataelement);
    List<Mapping> findByCATEGORYCOMBOSPECIFICAndDATAELEMENTSPECIF(String combo,String dataelement);
    List<Mapping> findByCATEGORYCOMBOYEARAndCATEGORYCOMBOGENDERAndCATEGORYCOMBOSPECIFICAndDATAELEMENTSPECIF(String year,String gender, String combo,String dataelement);
    List<Mapping> findByDENMAPPER(String dataelement);
    List<Mapping> findByCATEGORYCOMBOSPECIFICAndCATEGORYCOMBOYEARAndDENMAPPER(String combo,String year,String mapper);
    List<Mapping> findByCATEGORYCOMBOSPECIFICAndDENMAPPER(String combo,String mapper);
    //List<Mapping> findByDATAELEMENTSPECIFAndCATEGORYCOMBOSPECIFICAndAndDENMAPPER(String combo,String mapper);


}
