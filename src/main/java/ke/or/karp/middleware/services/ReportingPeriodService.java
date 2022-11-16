package ke.or.karp.middleware.services;

import ke.or.karp.middleware.model.ReportingPeriod;
import ke.or.karp.middleware.model.Role;
import ke.or.karp.middleware.repositories.ReportingPeriodRepository;
import ke.or.karp.middleware.repositories.RoleRepository;
import ke.or.karp.middleware.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("reportingPeriodService")
public class ReportingPeriodService {

    private UserRepository userRepository;
    private ReportingPeriodRepository reportingPeriodRepository;

    Date nowDate = new Date();
    @Autowired
    public ReportingPeriodService(UserRepository userRepository,
                                  ReportingPeriodRepository reportingPeriodRepository
                       ) {
        this.userRepository = userRepository;
        this.reportingPeriodRepository = reportingPeriodRepository;
    }
    public List<ReportingPeriod> getAll(){return  reportingPeriodRepository.findAll();}
    public List<ReportingPeriod> getAllCurrentAndPast(int id ){return  reportingPeriodRepository.findByIdLessThanEqualOrderByStartdateDesc(id);}


    public ReportingPeriod getByQuarter(String q){
        return  reportingPeriodRepository.findByQuarter(q);
    }
    public List<ReportingPeriod> getByStatus(int status){
        return  reportingPeriodRepository.getByStatus(status);
    }

    public ReportingPeriod save(ReportingPeriod reportingPeriod) {
        return reportingPeriodRepository.save(reportingPeriod);
    }
}
