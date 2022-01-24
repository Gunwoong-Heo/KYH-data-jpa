package study.KYHdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.KYHdatajpa.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
