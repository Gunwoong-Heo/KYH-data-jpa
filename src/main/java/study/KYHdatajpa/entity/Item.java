package study.KYHdatajpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
//@Getter
@EntityListeners(AuditingEntityListener.class)  // @CreatedDate 때문에 추가
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Item {
// 실무에서는 `@GeneratedValue`를 못 쓸때도 있다.(데이터가 너무 많고, 특수한 상황에서 테이블을 분할 하거나 등등)
// 이럴때는 id를 임의로 생성해야 할때도 있음. 이럴때는 `Persistable`을 사용. (@Getter를 생략 후 사용한다. `getId()`를 Override 해야하기 때문)
public class Item implements Persistable<String> {

/*
    @Id
    @GeneratedValue
    private Long id;
*/

    @Id
    private String id;

    // BaseEntity를 상속받아서 구현해도 됨.
    @CreatedDate
    private LocalDateTime createdDate;

    public Item(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}
