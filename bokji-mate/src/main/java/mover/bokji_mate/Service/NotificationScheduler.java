package mover.bokji_mate.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mover.bokji_mate.domain.Member;
import mover.bokji_mate.domain.Policy;
import mover.bokji_mate.repository.PolicyRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {

    private final PolicyRepository policyRepository;
    private final NotificationService notificationService;


    @Scheduled(cron = "0 0 9 * * ?")    // 매일 오전 9시에 실행
    public void sendDeadlineNotifications() {
        LocalDate alertDate = LocalDate.now().plusDays(3);
        List<Policy> findPolicies = policyRepository.findByEndDate(alertDate);

        findPolicies.forEach(policy -> {
            policy.getScraps().forEach(scrap -> {
                Member member = scrap.getMember();
                String message = policy.getTitle() + " 정책이 곧 마감됩니다.";
                log.info("마감 임박 정책 발견");
                notificationService.sendNotification(member, policy, message);
            });
        });

    }
}
