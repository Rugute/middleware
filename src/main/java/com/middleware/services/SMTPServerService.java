package com.middleware.services;

import com.middleware.model.SMTPServer;
import com.middleware.repositories.SMTPServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("smtpServerService")
public class SMTPServerService {
    private SMTPServerRepository smtpServerRepository;
    Date nowDate = new Date();
    @Autowired
    public SMTPServerService(SMTPServerRepository smtpServerRepository) {
        this.smtpServerRepository = smtpServerRepository;

    }
    public SMTPServer saveSMTPServer(SMTPServer smtpServer) {
        return smtpServerRepository.save(smtpServer);
    }

    public SMTPServer getBySectionID(String id){
        return  smtpServerRepository.findBySectionid(id);
    }
    public List<SMTPServer> getAllSMTPServer(){
        return  smtpServerRepository.findAll();
    }
    public SMTPServer getByHost(String host){
        return  smtpServerRepository.findByHost(host);
    }
    public SMTPServer getByTopOne(){
        return  smtpServerRepository.findFirstByOrderByIdAsc();
    }




}

