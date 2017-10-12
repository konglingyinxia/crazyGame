package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.WxConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * Created by 田帅 on 2017/10/11/011.
 */
public interface WxConfigRepository extends JpaRepository<WxConfig, String> {

    @Query(value = "UPDATE bm_wx_config SET typevalue=:typevalue WHERE id= :id",nativeQuery = true)
    @Modifying
    @Transactional
    public abstract int setTypevalueById(@Param("id") String id, @Param("typevalue") String typevalue) ;

    @Query(value = "select * from bm_wx_config WHERE id= :id",nativeQuery = true)
    public abstract WxConfig selectTicket(@Param("id") String id) ;
}
