package study.KYHdatajpa.repository;

import lombok.RequiredArgsConstructor;
import study.KYHdatajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
// 강의내용 : 구현체에서 `리포지토리이름+Impl` 을 반드시 맞춰야한다. (Imple 대신 다른 이름으로 변경하고 싶으면, `XML설정이나,JavaConfig`로 해야한다(메뉴얼참조). 하지만 웬만하면 관례를 따르는게 낫다.)
// 보충내용 : 스프링 데이터 2.x 부터는 사용자 정의 구현 클래스에 리포지토리 인터페이스 이름 + Impl 을 적용하는
//            대신에 사용자 정의 인터페이스 명 + Impl 방식도 지원한다.
//            예를 들어서 위 예제의 MemberRepositoryImpl 대신에 MemberRepositoryCustomImpl 같이 구현해도 된다.
//            기존 방식보다 이 방식이 사용자 정의 인터페이스 이름과 구현 클래스 이름이 비슷하므로 더 직관적이다.
//            추가로 여러 인터페이스를 분리해서 구현하는 것도 가능하기 때문에 새롭게 변경된 이 방식을 사용하는
//            것을 더 권장한다.
//참고: 항상 사용자 정의 리포지토리가 필요한 것은 아니다. 그냥 임의의 리포지토리를 만들어도 된다.
//      예를들어 MemberQueryRepository를 인터페이스가 아닌 클래스로 만들고 스프링 빈으로 등록해서(@Repository)
//      그냥 직접 사용해도 된다. 물론 이 경우 스프링 데이터 JPA와는 아무런 관계 없이 별도로 동작한다.
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }

}