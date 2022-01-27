package study.KYHdatajpa.repository;

import study.KYHdatajpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}