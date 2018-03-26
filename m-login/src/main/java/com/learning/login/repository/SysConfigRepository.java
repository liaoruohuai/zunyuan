package com.learning.login.repository;

import com.learning.login.entity.SysConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysConfigRepository extends JpaRepository<SysConfig,Integer> {
    SysConfig findByKeyName(String keyName);
}
