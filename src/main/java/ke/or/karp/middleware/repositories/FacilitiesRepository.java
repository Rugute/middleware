package ke.or.karp.middleware.repositories;

import ke.or.karp.middleware.model.Dataset;
import ke.or.karp.middleware.model.Facilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository("facilitiesRepository")
public interface FacilitiesRepository extends JpaRepository<Facilities, Long> {
    List<Facilities> findAll();
    Facilities findByMflcode(double qid);
    Facilities findByFacilityname(String qid);
    Facilities findByFacilitynameContaining(String qid);
    Facilities findByFacilitynameLike(String qid);
    Facilities findByFacilitynameEndsWith(String director);
    @Query("SELECT m FROM Facilities m WHERE m.facilityname LIKE %:title%")
    List<Facilities> searchByFnameLike(@Param("title") String title);
    @Query("SELECT m FROM Facilities m WHERE m.ftype LIKE %:title%")
    List<Facilities> searchByFtypeLike(@Param("title") String title);
    //Facilities findByorgUnitName(String fname);
}

