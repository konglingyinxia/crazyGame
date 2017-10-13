package com.crazy.web.service.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.crazy.web.model.PresentApp;

/**
 * @ClassName: PresentAppRepository
 * @Description: TODO(提现记录表)
 * @author dave
 * @date 2017年9月25日 下午8:02:17
 */
public abstract interface PresentAppRepository extends JpaRepository<PresentApp, String>, JpaSpecificationExecutor<PresentApp> {

	public abstract PresentApp findById(String id);

}
