package com.learning.login.repository;

import com.learning.login.entity.SmsLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2017/12/10.
 */
public interface SmsLogRepository extends JpaRepository<SmsLog,Integer> {

}
