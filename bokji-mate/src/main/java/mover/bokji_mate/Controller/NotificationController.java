package mover.bokji_mate.Controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mover.bokji_mate.Service.NotificationService;
import mover.bokji_mate.jwt.JwtTokenProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final JwtTokenProvider jwtTokenProvider;
    private final NotificationService notificationService;
    public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @GetMapping("/subscribe")
    public SseEmitter subscribe(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        log.info("뭐가문제일까1");
        SseEmitter sseEmitter = notificationService.subscribe(accessToken);
        return sseEmitter;
    }
}
