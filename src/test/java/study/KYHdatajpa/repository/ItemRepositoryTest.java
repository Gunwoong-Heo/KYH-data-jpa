package study.KYHdatajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import study.KYHdatajpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void save() {
/*
        Item item = new Item();
        itemRepository.save(item);  // `@GeneratedValue`는 jpa에 persist를 하면 그때 들어감
*/
        // 디버그 모드로 들어가면 `SimpleJpaRepository`의 `save(S entity)`에서 `entityInformation.isNew(entity)`가 false로 나옴 (식별자가 객체일 때 `null`로 판단하기 때문) -> merge 실행
        // merge 하기 위해 select 해도 안 나옴 -> 다시 persist 실행. insert쿼리 날림  (처음부터 psersist(insert)하면 될 것을, 불필요하게 쿼리를 1번 select 하게 됨 -> 성능 낭비)_
        // 그리고 기본적으로 merge는 사용하지 않는다고 생각하는 편이 좋다.
        // merge는 특수한 경우에만 사용한다. 변경에는 dirtyChecking을 사용하고 저장에는 persist를 사용.
        Item item = new Item("A");
        itemRepository.save(item);  // `@GeneratedValue`는 jpa에 persist를 하면 그때 들어감
    }

}