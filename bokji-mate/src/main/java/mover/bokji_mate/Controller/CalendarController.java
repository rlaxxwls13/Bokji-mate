package mover.bokji_mate.Controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mover.bokji_mate.Service.PolicyService;
import mover.bokji_mate.domain.Scrap;
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
    @GetMapping("/scraps")
    public ResponseEntity<List<Scrap>> getScraps(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        List<Scrap> scraps = policyService.getScraps(accessToken);

        return ResponseEntity.ok(scraps);
    }
}
