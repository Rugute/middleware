package com.middleware.repositories;

import com.middleware.model.ETL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface ETLRepository extends JpaRepository<ETL, Long> {
    @Query(value = "CALL sp_get_patient_programs();", nativeQuery = true)
   // List<Car> findCarsAfterYear(@Param("year_in") Integer year_in);
   // @Procedure(name = "middlewarelive.sp_get_patient_programs")
    Map<String, Object> testSp();
    //Map<String, Object> testSp(@Param("DOC_NAME") String docName);
}
