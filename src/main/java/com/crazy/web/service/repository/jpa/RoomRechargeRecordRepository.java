package com.crazy.web.service.repository.jpa;


import com.crazy.web.model.RoomRechargeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 田帅 on 2017/9/25/025.
 */

public interface RoomRechargeRecordRepository extends JpaRepository<RoomRechargeRecord, String>,JpaSpecificationExecutor<RoomRechargeRecord> {
}
