package com.learning.login.repository;

import com.learning.login.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
/**
 * Author:LRH
 * Date:2017/12/11
 * Discription:
 */
public interface MemberRepository extends JpaRepository<Member,String> {

    Member findByMemberPhone(String memberPhone);

    @Query("select max (memberId) from Member")
    String findMaxMemberId();

    Page<Member> findAll(Specification<Member> whereClause, Pageable pageable);

    Member  findByMemberId(String MemberId);
}
