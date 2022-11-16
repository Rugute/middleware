package ke.or.karp.middleware.repositories;

import ke.or.karp.middleware.model.DataElementGroups;
import ke.or.karp.middleware.model.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataElementGroupsRepository extends JpaRepository<DataElementGroups, Long> {
    List<DataElementGroups> findAll();
    DataElementGroups findByDisplayid(String qid);
    List<DataElementGroups> findByStatus(String status);
}

