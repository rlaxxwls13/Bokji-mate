package mover.bokji_mate.Service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mover.bokji_mate.domain.Member;
import mover.bokji_mate.dto.JwtToken;
import mover.bokji_mate.dto.MemberDto;
import mover.bokji_mate.dto.SignUpDto;
import mover.bokji_mate.jwt.JwtTokenProvider;
import mover.bokji_mate.repository.MemberRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    @Transactional
    public JwtToken signIn(String username, String password) {
        // 1. username + password 기반으로 Authentication 객체 생성
        // 이 때 Authentication은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증. authenticate()를 통해 요청된 Member에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailService에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        long refreshTokenExpirationMillis = jwtTokenProvider.getRefreshTokenExpirationMillis();
        redisService.setValues(authentication.getName(), jwtToken.getRefreshToken(), Duration.ofMillis(refreshTokenExpirationMillis));

        return jwtToken;
    }

    @Transactional
    public MemberDto signUp(SignUpDto signUpDto) {
        /*if (memberRepository.existsByUsername(signUpDto.getUsername())){
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }
         */
        validateDuplicateMember(signUpDto);
        //password 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        List<String> roles = new ArrayList<>();
        roles.add("USER");  //USER 권한 부여
        return MemberDto.toDto(memberRepository.save(signUpDto.toEntity(encodedPassword, roles)));
    }

    private void validateDuplicateMember(SignUpDto signUpDto) {
        Optional<Member> findMembers = memberRepository.findByUsername(signUpDto.getUsername());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 사용 중인 사용자 이름입니다.");
        }
    }

    @Transactional
    public void signOut(String refreshToken, String accessToken) {
        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        String username = claims.getSubject();
        if (redisService.checkExistsValue(refreshToken)) {
            redisService.deleteValues(username);

            // 로그아웃시 Access Token 블랙리스트에 저장
            long accessTokenExpirationMillis = jwtTokenProvider.getAccessTokenExpirationMillis();
            redisService.setValues(accessToken, "logout", Duration.ofMillis(accessTokenExpirationMillis));
        }

    }

}
