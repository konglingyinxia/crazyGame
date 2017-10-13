package com.crazy.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crazy.web.model.ProManagement;

/**
 * @ClassName: ProManagementRepository
 * @Description: TODO(提现历史)
 * @author dave
 * @date 2017年9月25日 下午8:03:31
 */
public abstract interface ProManagementRepository extends JpaRepository<ProManagement, String>, JpaSpecificationExecutor<ProManagement> {

	@Query(value = "select * from bm_pro_management where 1 = 1 and USER_NAME like %:userName% and INVITATION_CODE like %:invitationCode% limit :page,:limit", nativeQuery = true)
	public abstract List<ProManagement> findByPrams(@Param("userName") String userName, @Param("invitationCode") String invitationCode, @Param("page") int page,
			@Param("limit") int limit);

}
