package com.crazy.web.service.repository.jpa;


import com.crazy.web.model.RunHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RunHistoryRepository extends JpaRepository<RunHistory, String>,JpaSpecificationExecutor<RunHistory> {

}
