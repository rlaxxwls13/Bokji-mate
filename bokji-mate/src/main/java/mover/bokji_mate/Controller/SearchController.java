package mover.bokji_mate.Controller;

import lombok.RequiredArgsConstructor;
import mover.bokji_mate.Service.PolicyService;
import mover.bokji_mate.Service.SearchService;
import mover.bokji_mate.dto.PolicyDto;
import mover.bokji_mate.repository.PolicyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class SearchController {

    private final PolicyService policyService;
    private final SearchService searchService;

    //인기 검색어 조회
    @GetMapping("search/popular")
    public ResponseEntity<List<String>> getPopularSearches() {
        List<String> popularKeywords = searchService.getTop5Keywords();
        return ResponseEntity.ok(popularKeywords);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PolicyDto>> getSearches(@RequestParam String keyword) {
        searchService.recordSearchKeyword(keyword);  // 검색어 기록
        List<PolicyDto> results = policyService.searchPolicy(keyword);  // 검색 수행
        return ResponseEntity.ok(results);  // 검색 결과 반환

    }
}
