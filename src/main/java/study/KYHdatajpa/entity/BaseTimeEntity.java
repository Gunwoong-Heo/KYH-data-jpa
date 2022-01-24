package study.KYHdatajpa.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
// 시간만 필요한 경우에는 `BaseTimeEntity`만 extends해서 사용
// 누가 등록했고, 수정했는지 까지는 알 필요가 없는 테이블들이 있기 때문에, `BaseTimeEntity`를 만들어서 `BaseEntity`에서 상속받게 만듬 
public class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

}
