package study.KYHdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.KYHdatajpa.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}