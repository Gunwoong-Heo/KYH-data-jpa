package study.KYHdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.KYHdatajpa.entity.Member;


public interface MemberRepository extends JpaRepository<Member, Long> {

}