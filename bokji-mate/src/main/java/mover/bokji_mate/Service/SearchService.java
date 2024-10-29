package mover.bokji_mate.Service;

import lombok.RequiredArgsConstructor;
import mover.bokji_mate.domain.SearchKeyword;
import mover.bokji_mate.repository.SearchKeywordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mover.bokji_mate.repository.SearchKeywordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchKeywordRepository searchKeywordRepository;

    // 검색 키워드 카운트
    @Transactional
    public void recordSearchKeyword(String keyword) {
        Optional<SearchKeyword> optionalKeyword = searchKeywordRepository.findByKeyword(keyword);
        if (optionalKeyword.isPresent()) {  //이미 검색당한 적이 있는 키워드인 경우 새로 추가
            SearchKeyword searchKeyword = optionalKeyword.get();
            searchKeyword.setSearchCount(searchKeyword.getSearchCount() + 1);
        } else {    //처음 검색된 키워드인 경우 새로 추가
            SearchKeyword newKeyword = SearchKeyword.builder()
                    .keyword(keyword)
                    .searchCount(1L)
                    .build();
            searchKeywordRepository.save(newKeyword);
        }
    }

    // 인기 검색어(상위 5개) 추출
    @Transactional(readOnly = true)
    public List<String> getTop5Keywords() {
        return searchKeywordRepository.findTop5ByOrderBySearchCountDesc().stream()
                .map(SearchKeyword::getKeyword)
                .collect(Collectors.toList());
    }

}
