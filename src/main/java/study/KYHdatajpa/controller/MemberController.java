package study.KYHdatajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.KYHdatajpa.dto.MemberDto;
import study.KYHdatajpa.entity.Member;
import study.KYHdatajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

/*
    // 도메인 클래스 컨버터 사용 X
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }
*/

    // 도메인 클래스 컨버터 사용
    // 개인적으로는 권장하지 않음.
    @GetMapping("/members/{id}")
    public String findMember2(@PathVariable("id") Member member) {  // 여기서 member는 딱 조회용으로만 사용해야됨(메뉴얼 참조)
        return member.getUsername();
    }

    @GetMapping("/members")
    // 기본설정을 바꾸고 싶으면, `application.yml`에 글로벌세팅 (spring.data.web.pageable.xxxxx)
//    public Page<Member> list(Pageable pageable) {
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "username") Pageable pageable) {  // `application.yml` 에 적용한 default세팅보다 `@PageableDefault`이 우선권이 높다.(당연히..)
        Page<Member> page = memberRepository.findAll(pageable);  // 엔티티를 외부에 노출하면 안됨. Dto로 변환해서 반환하자.
//        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        // Dto는 엔티티를 보아도 된다(의존관계 측면에서). 엔티티는 가급적 Dto를 보지 않는 편이좋다.
        // Dto에 member를 파라미터로 받는 생성자를 만들어서 활용
        Page<MemberDto> map = page.map(member -> new MemberDto(member));
        return map;
/*
        // 이렇게 축약 가능
        return memberRepository.findAll(pageable)
//                .map(member -> new MemberDto(member));
                .map(MemberDto::new);  // `Replace lambda with method reference` 로 한번 더 줄임
*/
    }
    // `ItemRepositoryTest`에서  `save메소드`를 디버거 모드로 보려면 `@PostConstruct` 잠깐 주석처리하기.
    // (주석처리 안 하고 `entityInformation.isNew(entity)`에 breakPoint 찍고 디버거 모드로 돌려버리면 Member가 걸려버림)
    // (디버거 모드로 확인만 한 후에 잊지말고 주석 제거할것)
    @PostConstruct
    public void init() {
//        memberRepository.save(new Member("userA"));
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member(("user" + i), i));
        }
    }

}