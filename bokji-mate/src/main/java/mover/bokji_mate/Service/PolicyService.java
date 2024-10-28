package mover.bokji_mate.Service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mover.bokji_mate.domain.Member;
import mover.bokji_mate.domain.Policy;
import mover.bokji_mate.domain.Scrap;
import mover.bokji_mate.dto.PolicyDto;
import mover.bokji_mate.jwt.JwtTokenProvider;
import mover.bokji_mate.repository.MemberRepository;
import mover.bokji_mate.repository.PolicyRepository;
import mover.bokji_mate.repository.ScrapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PolicyService {
    private final PolicyRepository policyRepository;
    private final ScrapRepository scrapRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    //정책 스크랩
    @Transactional
    public void scrapPolicy(String accessToken, Long id) {
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String username = claims.getSubject();
        Member findMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member is not found"));
        Policy findPolicy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy is not found"));

        // 중복 스크랩 확인
        if (scrapRepository.existsByMemberAndPolicy(findMember, findPolicy)) {
            throw new RuntimeException("Policy already scrapped by this user.");
        }

        // 스크랩 추가
        Scrap scrap = Scrap.builder()
                .member(findMember)
                .policy(findPolicy)
                .build();
        scrapRepository.save(scrap);
        findMember.setScrapCount(findMember.getScrapCount() + 1);
        findPolicy.setScrapCount(findPolicy.getScrapCount() + 1);
    }

    //스크랩 삭제
    @Transactional
    public void deleteScrap(String accessToken, Long id) {
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String username = claims.getSubject();
        Member findMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member is not found"));
        Policy findPolicy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy is not found"));
        Scrap findScrap = scrapRepository.findByMemberAndPolicy(findMember, findPolicy)
                .orElseThrow(() -> new RuntimeException("Scrap is not found"));

        scrapRepository.delete(findScrap);
        findMember.setScrapCount(findMember.getScrapCount() - 1);
        findPolicy.setScrapCount(findPolicy.getScrapCount() - 1);
    }

    // 스크랩한 정책 전송
    @Transactional
    public List<Scrap> getScraps(String accessToken) {
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String username = claims.getSubject();

        return scrapRepository.findByMemberUsername(username);
    }

    @Transactional
    public PolicyDto viewPolicy(Long id) {
        Policy findPolicy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy is not found"));
        findPolicy.setViews(findPolicy.getViews() + 1);
        PolicyDto policyDto = PolicyDto.toDto(findPolicy);
        return policyDto;
    }




}
