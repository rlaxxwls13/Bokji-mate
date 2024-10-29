package mover.bokji_mate.Controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mover.bokji_mate.Service.PolicyService;
import mover.bokji_mate.domain.Policy;
import mover.bokji_mate.domain.Scrap;
import mover.bokji_mate.dto.PolicyDto;
import mover.bokji_mate.jwt.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calendar")
@Slf4j
@RequiredArgsConstructor
public class CalendarController {

    private final PolicyService policyService;
    private final JwtTokenProvider jwtTokenProvider;

    // 캘린더에 멤버가 스크랩한 정책 보내기
    @GetMapping("/getScraps")
    public ResponseEntity<List<PolicyDto>> getScraps(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        List<PolicyDto> policies = policyService.getScraps(accessToken);
        // 정책 목록이 비어있을 경우 204 No Content 반환
        if (policies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(policies);
    }
}
