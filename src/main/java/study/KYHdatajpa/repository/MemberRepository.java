package study.KYHdatajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.KYHdatajpa.dto.MemberDto;
import study.KYHdatajpa.entity.Member;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String name, int age);

    List<Member> findTop3HelloBy();

//    @Query(name = "Member.findByUsername")  // 주석처리해도 동작함: 관례상 엔티티명.메소드이름 으로 먼저 namedQuery를 찾는다. 없으면, 메소드 이름으로 쿼리를 생성하는 것을 함(쿼리메소드).
    List<Member> findByUsername(@Param("username") String username);

    // Application 로딩 시점에 파싱을 해보고, 문법오류가 있으면 로딩 시저에 에러를 띄워서 알려준다.
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // dto로 조회할 때는 new 를 사용해야함.
    @Query("select new study.KYHdatajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
//    List<Member> findByNames(@Param("names") List<String> names);
    List<Member> findByNames(@Param("names") Collection<String> names);  // 다른 Collection들도 받을 수 있도록, List 보다 상위인 Collection으로 변경

    List<Member> findListByUsername(String username);  // 컬렉션
    Member findMemberByUsername(String username);  // 단건
    Optional<Member> findOptionalByUsername(String username);  // 단건 Optional

    // 반환 타입의 따라 데이터set이 달라짐
    List<Member> findListByAge(int age, Pageable pageable);
    Page<Member> findByAge(int age, Pageable pageable);
    Slice<Member> findSliceByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findQueryByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    // 혼합형도 가능
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // `@NamedEntityGraph` 를 활용하는 방법도 있음 -> 잘 안 씀. `attributePaths`를 사용하는 방식으로 하거나, 더 복잡한 것들은 jpql을 직접짜는 방식으로 해결
//    @EntityGraph("Member.all") 
    @EntityGraph(attributePaths = ("team"))
    List<Member> findEntityGraphByUsername(@Param("username") String username);

}