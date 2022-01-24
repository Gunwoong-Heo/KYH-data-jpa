package study.KYHdatajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})  // team을 넣으면 무한루프에 빠질 가능성이 있음
// `NamedQuery`는 실무에서는 잘 안씀
@NamedQuery(
        name="Member.findByUsername",
        query = "select m from Member m where m.username = :username"  // Application 로딩 시점에 파싱을 해보고, 문법오류가 있으면 로딩 시저에 에러를 띄워서 알려준다.
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    // JPA가 proxy 기술을 사용하게 되는 경우가 있으므로, private으로 만들어 놓으면 에러가 발생할 수 있다. 최소 protected로 열어 놓아야 한다.
    // Lombok으로 대체
//    protected Member() {
//
//    }

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    // 연관관계 편의 메서드
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    //setter 대신에 이렇게 구체적인 메소드를 사용해야 추적이 쉬운 등 장점이 많다.
//    public void changeUserName(String username) {
//        this.username = username;
//    }
}