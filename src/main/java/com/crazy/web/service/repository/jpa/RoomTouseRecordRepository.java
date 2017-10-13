package com.crazy.web.service.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crazy.web.model.RoomTouseRecord;

import java.util.List;

/**
 * Created by 田帅 on 2017/9/25/025.
 */
public abstract interface RoomTouseRecordRepository extends JpaRepository<RoomTouseRecord, String> {

    /**
     * 查询总条数
     * @param userName
     * @param invitationCode
     * @return
     */
    @Query(value = "select count(*) from bm_room_touse_record where USER_NAME LIKE  CONCAT('%',:userName,'%') and INVITATION_CODE LIKE CONCAT('%',:invitationCode,'%') ",nativeQuery = true)
    public abstract  int findToUseCount(@Param("userName") String userName, @Param("invitationCode") String invitationCode);

    /**]
     * 根据用户名邀请码查询房卡充值记录
     * @param userName
     * @param invitationCode
     * @return
     */
    @Query(value = "select *,DATE_FORMAT(USE_TIME,'%Y-%m-%d %H:%i:%S ') use_times from bm_room_touse_record where USER_NAME LIKE  CONCAT('%',:userName,'%') and INVITATION_CODE LIKE CONCAT('%',:invitationCode,'%')  limit :startData,:limit",nativeQuery = true)
    public abstract List<RoomTouseRecord> findByUserNameLikeOrInvitationCodeLike(@Param("userName") String userName, @Param("invitationCode") String invitationCode, @Param("startData")Integer startData, @Param("limit")Integer limit);
}
