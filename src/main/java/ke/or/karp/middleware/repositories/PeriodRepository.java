package ke.or.karp.middleware.repositories;

import ke.or.karp.middleware.model.Dataset;
import ke.or.karp.middleware.model.Months;
import ke.or.karp.middleware.model.Period;
import ke.or.karp.middleware.model.Years;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeriodRepository extends JpaRepository<Period, Long> {
    List<Period> findAllByOrderByIdDesc();
    Period findByDisplayid(String qid);
   }

