package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.AccountConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface AccountConfigRepository extends JpaRepository<AccountConfig, String>
{
  public abstract List<AccountConfig> findByOrgi(String orgi);
}
