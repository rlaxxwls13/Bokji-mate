package mover.bokji_mate.repository;

import mover.bokji_mate.domain.Member;
import mover.bokji_mate.domain.Policy;
import mover.bokji_mate.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    List<Scrap> findByMemberUsername(String username);
    boolean existsByMemberAndPolicy(Member member, Policy policy);
    Optional<Scrap> findByMemberAndPolicy(Member member, Policy policy);
}
