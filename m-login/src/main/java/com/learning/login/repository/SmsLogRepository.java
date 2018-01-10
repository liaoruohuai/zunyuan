package com.learning.login.repository;

import com.learning.login.entity.SmsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */
public interface SmsLogRepository extends JpaRepository<SmsLog,Integer> {

    /*@Query("select id from smslog where phoneNum = ?1 and smsDate >= ?2",nativeQuery = true)*/
    List<SmsLog> findSmsLogsByPhoneNumIsAndSmsDateGreaterThanEqual(String phoneNum, String smsDate);
}
