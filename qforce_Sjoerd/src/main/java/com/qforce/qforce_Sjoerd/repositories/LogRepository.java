package com.qforce.qforce_Sjoerd.repositories;

import com.qforce.qforce_Sjoerd.models.DatabaseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<DatabaseLog, String> {
}
