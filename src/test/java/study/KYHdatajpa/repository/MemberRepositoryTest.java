package study.KYHdatajpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.KYHdatajpa.dto.MemberDto;
import study.KYHdatajpa.entity.Member;
import study.KYHdatajpa.entity.Team;

import javax.persistence.NonUniqueResultException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember() {
        System.out.println("memberRepository = " + memberRepository.getClass());
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        // Optional로 반환되기 때문에 원칙적으로는 null 체크를 해야한다. 여기서는 예제라서 생략..
        // `.get()`으로 들어가보면 null 일때 `NoSuchElementException`이 터지는 것을 확인할 수 있다.
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);

    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회
        Member findMember1 = memberRepository.findById(member1.getId()).get();  // 원칙적으로는 `.get()`으로 바로 꺼내오면 안되고 Null 체크를 해야한다.
        Member findMember2 = memberRepository.findById(member2.getId()).get();  // 원칙적으로는 `.get()`으로 바로 꺼내오면 안되고 Null 체크를 해야한다.
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long removedCount = memberRepository.count();
        assertThat(removedCount).isEqualTo(0);

    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findHelloBy() {
        List<Member> helloBy = memberRepository.findTop3HelloBy();
    }

    @Test
    public void testNamedQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member( "AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member( "AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member( "AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);
        Member m1 = new Member("AAA", 10);
        m1.changeTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);  // 출력결과 : dto = MemberDto(id=2, username=AAA, teamName=teamA)    <- 출력결과가 이렇게 나오는 이유는 `MemberDto`에 `@Data`에 의해 `toString` 이 세팅 되었기 때문
        }
    }

    @Test
    public void findByNames() {
        Team team = new Team("teamA");
        teamRepository.save(team);
        Member m1 = new Member("AAA", 10);
        m1.changeTeam(team);
        memberRepository.save(m1);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> members = memberRepository.findListByUsername("AAA");
        System.out.println("members = " + members);

        List<Member> members2 = memberRepository.findListByUsername("dsafsadfsadfsadfasdf");  // 컬렉션을 반환받을 때에는, 데이터가 없으면 Empty컬렉션을 반환함 (반환되는 컬렉션은 null이 아님이 보장된다.  `if(!null)` 이런식으로 코드 짜지 말것 )
        System.out.println("members2 = " + members2);
        System.out.println("members2.size() = " + members2.size());

/*
        Member findMember = memberRepository.findMemberByUsername("AAA");  // 단건 조회에서 2개 이상이 나오면 Exception 터짐
        System.out.println("findMember = " + findMember);
*/

        // jpa는 singleResult를 조회했을떄 없으면, `NoResultException`을 터트림
        // SpringDataJpa는 내부적으로 try,catch 한 후에 null을 반환함
        Member findMember2 = memberRepository.findMemberByUsername("dsafqwf32532fsdafsd");  // 단건을 반환받을 때에는, 데이터가 없으면 null 이 반환됨.
        System.out.println("findMember2 = " + findMember2);

        // java 8 부터는 Optional로 감싼것을 반환받으면 null 문제에서 비교적 자유로워짐
/*
        Optional<Member> optionalUser = memberRepository.findOptionalByUsername("AAA");
        System.out.println("optionalUser = " + optionalUser);
*/
        Optional<Member> optionalUser2 = memberRepository.findOptionalByUsername("asdfsadfasdfsdasadfsad");
        System.out.println("optionalUser2 = " + optionalUser2);  // 출력결과 : optionalUser2 = Optional.empty

        // 단건 조회로 했는데 데이터가 2건이 나오면?
        // `javax.persistence.NonUniqueResultException` 터짐 -> SpringDataJpa가 `org.springframework.dao.IncorrectResultSizeDataAccessException` 로 바꿔서 반환해줌
        // 왜냐하면, repository의 기술은 jpa가 될 수도 있고, mongoDB가 될 수도 있고, 다른 기술이 될수도 있다. 
        // 그걸 사용하는 서비스계층의 클라이언트 코드들은 jpa 같은 구체적인 기술에 의존하는게 아니라,
        // 그냥 스프링이 추상화한 예외에 의존하면, 하부의 repository의 기술들을 MongoDB나 다른 jdbc나 이런것들로 바꾸어도
        // 스프링은 동일하게 데이터가 안 맞는 것은 `IncorrectResultSizeDataAccessException` 을 내려주면,
        // 이것을 사용하는 클라이언트 코드를 바꿀 필요가 없다. 그런 이유로 이런식으로 예외를 변환해서 반환을 해줌.
        // SpringDataJpa를 사용하면 이런 메커니즘까지 같이 제공이됨.
/*
        Optional<Member> findMember3 = memberRepository.findOptionalByUsername("AAA");
        System.out.println("findMember3 = " + findMember3);
*/
        Assertions.assertThrows(IncorrectResultSizeDataAccessException.class, () -> memberRepository.findOptionalByUsername("AAA"));
//        Assertions.assertThrows(NonUniqueResultException.class, () -> memberRepository.findOptionalByUsername("AAA"));  // 에러남 (`IncorrectResultSizeDataAccessException` 이 반환되기 때문)

    }

    @Test  // findByAge
    public void paging() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
//        Page<Member> page = memberRepository.findTop3ByAge(age, pageRequest);

        // then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();
//        for (Member member : content) {
//            System.out.println("member = " + member);
//        }
//        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

    }

    @Test  // findSliceByAge
    public void pagingSlice() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

        // then
        List<Member> content = page.getContent();

        assertThat(content.size()).isEqualTo(3);
//        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
//        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test  // findQueryByAge
    public void pagingQuery() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
//        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);
        Page<Member> page = memberRepository.findQueryByAge(age, pageRequest);
        // API같은 경우는 엔티티를 반환하면 안되고 반드시 dto로 변환하여 반환해야함 (KYH JPA 활용편2 참고)
        Page<MemberDto> map = page.map(m -> new MemberDto(m.getId(), m.getUsername(), m.getTeam().getName()));

        // then
        List<Member> content = page.getContent();

        assertThat(content.size()).isEqualTo(3);
//        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
//        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

}