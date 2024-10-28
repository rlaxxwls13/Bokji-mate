package mover.bokji_mate.Service;

import lombok.extern.slf4j.Slf4j;
import mover.bokji_mate.domain.Policy;
import mover.bokji_mate.domain.Scrap;
import mover.bokji_mate.dto.JwtToken;
import mover.bokji_mate.dto.SignInDto;
import mover.bokji_mate.dto.SignUpDto;
import mover.bokji_mate.repository.PolicyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class PolicyServiceTest {

    @Autowired
    PolicyService policyService;
    @Autowired
    MemberService memberService;
    @Autowired
    PolicyRepository policyRepository;

    private SignUpDto signUpDto;
    private String accessToken;

    @BeforeEach
    void beforeEach() {
        signUpDto = SignUpDto.builder()
                .username("member")
                .password("12345678")
                .nickname("닉네임")
                .phoneNumber("01012341234")
                .birthDate(LocalDate.parse("2001-07-13"))
                .interests(List.of("관심사1", "관심사2", "관심사3"))
                .build();

        // 회원가입
        memberService.signUp(signUpDto);

        // 로그인
        SignInDto signInDto = SignInDto.builder()
                .username("member")
                .password("12345678")
                .build();

        accessToken = memberService.signIn(signInDto.getUsername(), signInDto.getPassword()).getAccessToken();

        log.info("access token = {}", accessToken);
    }


    // 정책 스크랩
    @Test
    void scrapPolicy() {
        //1. 정책 스크랩
        Policy policy = Policy.builder()
                .title("정책1")
                .build();
        policyRepository.save(policy);
        log.info("policy id = {}", policy.getId());

        log.info("scrapCount before scrap = {}", policy.getScrapCount());

        policyService.scrapPolicy(accessToken, policy.getId());

        //2. 스크랩한 정책 확인
        List<Scrap> scraps = policyService.getScraps(accessToken);
        assertFalse(scraps.isEmpty(), "스크랩한 정책이 존재해야 합니다.");
        assertTrue(scraps.stream().anyMatch(scrap -> scrap.getPolicy().getId().equals(policy.getId())),
                "스크랩한 정책 목록에 정책이 포함되어야 합니다.");

        log.info("scrapCount after scrap = {}", policy.getScrapCount());
    }

    // 스크랩 삭제
    @Test
    void deleteScrap() {
        //1. 정책 스크랩
        Policy policy = Policy.builder()
                .title("정책1")
                .build();
        policyRepository.save(policy);
        policyService.scrapPolicy(accessToken, policy.getId());

        //2. 정책 삭제
        policyService.deleteScrap(accessToken, policy.getId());

        //3. 삭제 확인
        List<Scrap> scrapsAfterDelete = policyService.getScraps(accessToken);
        assertFalse(scrapsAfterDelete.stream()
                        .anyMatch(scrap -> scrap.getPolicy().getId().equals(policy.getId())),
                "삭제된 정책이 스크랩 목록에 존재해서는 안됩니다.");
        log.info("scraps = {}", policy.getScrapCount());
    }

    @Test
    void viewPolicy() {
        //1. DB에 정책 저장
        Policy policy = Policy.builder()
                .title("정책1")
                .build();
        policyRepository.save(policy);

        log.info("views = {}", policy.getViews());

        //2. 정책 조회하기

        //3. 조회수 확인
    }
}