package com.middleware.repositories;

import com.middleware.model.SMTPServer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SMTPServerRepository extends JpaRepository<SMTPServer, Long> {
    SMTPServer findBySectionid(String sid);
    List<SMTPServer> findAll();
    SMTPServer findByHost(String host);
    SMTPServer findFirstByOrderByIdAsc();
}