package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.PlayUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

public abstract interface PlayUserRepository extends JpaRepository<PlayUser, String>, JpaSpecificationExecutor<PlayUser> {

	public abstract PlayUser findById(String paramString);

	public abstract PlayUser findByUsername(String username);

	public abstract PlayUser findByEmail(String email);

	public abstract PlayUser findByUsernameAndPassword(String username, String password);

	public abstract Page<PlayUser> findByDatastatus(boolean datastatus, String orgi, Pageable paramPageable);

	public abstract Page<PlayUser> findByDatastatusAndUsername(boolean datastatus, String orgi, String username, Pageable paramPageable);

	public abstract PlayUser findByOpenid(String openid);

	@Query(value = "update bm_playuser set pinvitationcode = :pinvitationcode,UPDATETIME = now() where id = :id", nativeQuery = true)
	@Modifying
	@Transactional
	public abstract void setPinvitationcodeById(@Param("pinvitationcode") String pinvitationcode, @Param("id") String id);

	public abstract PlayUser findByInvitationcode(@Param("invitationcode") String pinvitationcode);

	public abstract List<PlayUser> findByPinvitationcode(String invitationcode);

	@Query(value = "update bm_playuser set cards = :cards,TRT_PROFIT = :trtProfit,UPDATETIME = now() where id = :id", nativeQuery = true)
	@Modifying
	@Transactional
	public abstract void setCardsAndTrtProfitById(@Param("cards") int cards, @Param("trtProfit") BigDecimal trtProfit, @Param("id") String id);

	@Query(value = "update bm_playuser set TRT_PROFIT = :trtProfit,UPDATETIME = now() where id = :id", nativeQuery = true)
	@Modifying
	@Transactional
	public abstract void setTrtProfitById(@Param("trtProfit") BigDecimal trtProfit, @Param("id") String id);

	@Query(value = "update bm_playuser set PP_AMOUNT = :trtProfit,UPDATETIME = now() where id = :id", nativeQuery = true)
	@Modifying
	@Transactional
	public abstract void setPpAmountById(@Param("trtProfit") BigDecimal trtProfit, @Param("id") String id);

	public abstract int countByPinvitationcode(String invitationcode);

	@Query(value = "select * from bm_playuser where 1 = 1 and NICKNAME like %:nickname% limit :page,:limit", nativeQuery = true)
	public abstract List<PlayUser> findByNickname(@Param("nickname") String nickname, @Param("page") int page, @Param("limit") int limit);

	@Query(value = "update bm_playuser set cards = :cards,UPDATETIME = now() where id = :id", nativeQuery = true)
	@Modifying
	@Transactional
	public abstract void setCardsById(@Param("cards") int cards, @Param("id") String id);

	public abstract PlayUser findByToken(String token);

	@Query(value = "update bm_playuser set TRT_PROFIT = :trtProfit, PP_AMOUNT = :ppAmount, UPDATETIME = now() where id = :id", nativeQuery = true)
	@Modifying
	@Transactional
	public abstract void setTrtProfitAndPpAmountById(@Param("trtProfit") BigDecimal trtProfit, @Param("ppAmount") BigDecimal ppAmount, @Param("id") String id);

	@Query(value = "update bm_playuser set ROOM_NUM = :roomNum WHERE ID = :id", nativeQuery = true)
	@Modifying
	@Transactional
	public abstract void setRoomNumById(@Param("roomNum") String roomNum, @Param("id") String id);

	@Query(value = "update bm_playuser set ROOM_NUM = NULL WHERE ID = :id", nativeQuery = true)
	@Modifying
	@Transactional
	public abstract void emptyRoomById(@Param("id") String id);
}
