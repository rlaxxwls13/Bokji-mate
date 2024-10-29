package mover.bokji_mate.repository;

import mover.bokji_mate.domain.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    List<Policy> findByTitleContainingIgnoreCase(String keyword);

    List<Policy> findByEndDate(LocalDate endDate);
}
