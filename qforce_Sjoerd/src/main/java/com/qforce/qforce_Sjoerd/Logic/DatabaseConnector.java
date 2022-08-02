package com.qforce.qforce_Sjoerd.Logic;

import com.qforce.qforce_Sjoerd.models.DatabaseLog;
import com.qforce.qforce_Sjoerd.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DatabaseConnector {
    public long counter = 0;
    private final LogRepository logRepository;

    @Autowired
    public DatabaseConnector(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void logSearch(String request) {
        DatabaseLog log = new DatabaseLog();

        log.setSearchRequest(request);
        log.setId(counter);
        log.setRequestType("SEARCH");

        counter++;

        logRepository.save(log);
    }

    public void logGet(String request) {
        DatabaseLog log = new DatabaseLog();

        log.setSearchRequest(request);
        log.setId(counter);
        log.setRequestType("GET");

        counter++;

        logRepository.save(log);
    }
}
