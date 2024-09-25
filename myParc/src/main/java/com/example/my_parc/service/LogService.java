package com.example.my_parc.service;

import com.example.my_parc.domain.Log;
import com.example.my_parc.domain.User;
import com.example.my_parc.repos.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public void createLog(User user, String action) {
        Log log = new Log();
        log.setUser(user);  // Store the entire User entity
        log.setAction(action);
        log.setDate(new Timestamp(System.currentTimeMillis()));

        logRepository.save(log);
    }

    public List<Log> findAllLogs() {
        return logRepository.findAll();
    }
}
