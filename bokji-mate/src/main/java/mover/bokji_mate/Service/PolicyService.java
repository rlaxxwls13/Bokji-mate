package mover.bokji_mate.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mover.bokji_mate.repository.PolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PolicyService {
    private final PolicyRepository policyRepository;

    //정책 스크랩
    @Transactional
    public void scrapPolicy(Long id) {

    }
}
