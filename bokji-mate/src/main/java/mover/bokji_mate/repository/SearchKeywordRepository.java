package mover.bokji_mate.repository;

import mover.bokji_mate.domain.SearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {
    Optional<SearchKeyword> findByKeyword(String keyword);
    List<SearchKeyword> findTop5ByOrderBySearchCountDesc();

}
