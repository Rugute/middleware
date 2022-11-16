package ke.or.karp.middleware.repositories;

import ke.or.karp.middleware.model.Alldata;
import ke.or.karp.middleware.model.DatabaseInfo;
import ke.or.karp.middleware.model.Facilities;
import ke.or.karp.middleware.model.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface DatabasesRepository extends JpaRepository<DatabaseInfo, Long> {
    /*public void init();
    public void save(MultipartFile file);
    public Resource load(String filename);
    public void deleteAll();*/
  //  public Stream<Path> loadAll();
    List<DatabaseInfo> findAll();
    DatabaseInfo findByDbname(String dbname);

}
