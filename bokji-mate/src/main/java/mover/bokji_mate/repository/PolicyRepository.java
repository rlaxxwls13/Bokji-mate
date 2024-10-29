package mover.bokji_mate.repository;

import io.lettuce.core.dynamic.annotation.Param;
import mover.bokji_mate.domain.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    @Query("SELECT DISTINCT p FROM Policy p LEFT JOIN p.categories c " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Policy> searchByKeyword(@Param("keyword") String keyword);
    List<Policy> findByEndDate(LocalDate endDate);
}
