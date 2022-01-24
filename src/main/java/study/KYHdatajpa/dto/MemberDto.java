package study.KYHdatajpa.dto;

import lombok.Data;
import study.KYHdatajpa.entity.Member;

// 엔티티에는 `@Data`를 웬만해서는 쓰면안된다. -> Getter,Setter 등등 다 들어가 있기 때문에
// 단순 조회용 dto에서는 사용 가능
@Data
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
    }
}
