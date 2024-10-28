package mover.bokji_mate.Controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mover.bokji_mate.Service.PolicyService;
import mover.bokji_mate.dto.PolicyDto;
import mover.bokji_mate.jwt.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/policy")
@Slf4j
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;
    private final JwtTokenProvider jwtTokenProvider;

    // 정책 스크랩
    @GetMapping("/scrap/{policyId}")
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
}
