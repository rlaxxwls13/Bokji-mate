package mover.bokji_mate.Controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mover.bokji_mate.Service.PolicyService;
import mover.bokji_mate.domain.Policy;
import mover.bokji_mate.dto.PolicyDto;
import mover.bokji_mate.jwt.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/policy")
@Slf4j
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;
    private final JwtTokenProvider jwtTokenProvider;

    // 정책 스크랩
    @PostMapping("/scrap/{policyId}")
    public ResponseEntity<String> scrapPolicy(HttpServletRequest request, @PathVariable Long policyId) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        policyService.scrapPolicy(accessToken, policyId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Policy scrapped successfully.");
    }

    // 스크랩한 정책 삭제
    @DeleteMapping("/scrap/{policyId}")
    public ResponseEntity<String> deleteScrap(HttpServletRequest request, @PathVariable Long policyId) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        policyService.deleteScrap(accessToken, policyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Policy scrap deleted successfully.");
    }

    // 정책 조회 (조회수 증가)
    @GetMapping("/{policyId}")
    public ResponseEntity<PolicyDto> viewPolicy(@PathVariable Long policyId) {
        PolicyDto policyDto = policyService.viewPolicy(policyId);
        return ResponseEntity.ok(policyDto);
    }

    // 전체 정책 전송
    @GetMapping("/all-policies")
    public ResponseEntity<List<PolicyDto>> getAllPolicies() {
        List<PolicyDto> allPolicies = policyService.getAllPolicies();
        return ResponseEntity.ok(allPolicies);
    }

    // 추천 정책 전송
    @GetMapping("/recommendation")
    public ResponseEntity<List<PolicyDto>> getRecommendation(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        List<PolicyDto> recommendation = policyService.getRecommendation(accessToken);
        return ResponseEntity.ok(recommendation);
    }
}
