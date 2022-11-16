package com.middleware.repositories;

import com.middleware.model.DatabaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DatabasesRepository extends JpaRepository<DatabaseInfo, Long> {
    /*public void init();
    public void save(MultipartFile file);
    public Resource load(String filename);
    public void deleteAll();*/
  //  public Stream<Path> loadAll();
    List<DatabaseInfo> findAll();
    DatabaseInfo findByDbname(String dbname);

}
