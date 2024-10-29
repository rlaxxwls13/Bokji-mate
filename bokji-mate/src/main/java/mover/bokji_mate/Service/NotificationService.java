package mover.bokji_mate.Service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mover.bokji_mate.Controller.NotificationController;
import mover.bokji_mate.domain.Member;
import mover.bokji_mate.domain.Notification;
import mover.bokji_mate.domain.Policy;
import mover.bokji_mate.jwt.JwtTokenProvider;
import mover.bokji_mate.repository.MemberRepository;
import mover.bokji_mate.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final JwtTokenProvider jwtTokenProvider;
    public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    // 메시지 알림
    public SseEmitter subscribe(String accessToken) {
        log.info("뭐가문제일까2");
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String username = claims.getSubject();
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Member is not found"));

        log.info("뭐가문제일까3");

        //현재 클라이언트를 위한 sseEmitter 생성
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        log.info("뭐가문제일까4");

        // user 의 pk 값을 key 값으로 해서 sseEmitter 를 저장
        NotificationController.sseEmitters.put(member.getId(), sseEmitter);

        log.info("뭐가문제일까5");

        sseEmitter.onCompletion(() -> NotificationController.sseEmitters.remove(member.getId()));
        sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(member.getId()));
        sseEmitter.onError((e) -> NotificationController.sseEmitters.remove(member.getId()));
        log.info("sseEmitter 저장");

        try {
            //연결
            sseEmitter.send(SseEmitter.event().name("connect"));
            log.info("sseEmitter 연결");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sseEmitter;
    }


    public void sendNotification(Member member, Policy policy, String message) {
        //DB에 알람 저장
        Notification notification = Notification.builder()
                .member(member)
                .policy(policy)
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);

        //저장한 알림을 해당 회원의 SSE Emitter로 전송
        SseEmitter sseEmitter = sseEmitters.get(member.getId());
        if (sseEmitter != null) {
            try {
                sseEmitter.send(SseEmitter.event().name("notification").data(message));
            } catch (IOException e) {
                e.printStackTrace();
                // 에러 발생 시 Emitter를 제거
                sseEmitters.remove(member.getId());
            }
        }
    }
}
