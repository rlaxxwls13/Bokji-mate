package mover.bokji_mate.Controller;

import lombok.extern.slf4j.Slf4j;
import mover.bokji_mate.Service.MemberService;
import mover.bokji_mate.Service.RedisService;
import mover.bokji_mate.dto.JwtToken;
import mover.bokji_mate.dto.MemberDto;
import mover.bokji_mate.dto.SignInDto;
import mover.bokji_mate.dto.SignUpDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class MemberControllerTest {

    @Autowired
    MemberService memberService;
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    RedisService redisService;
    @Value(value = "${local.server.port}")
    private int port;

    private SignUpDto signUpDto;

    @BeforeEach
    void beforeEach() {
        signUpDto = SignUpDto.builder()
                .username("member")
                .password("12345678")
                .nickname("닉네임")
                .build();
    }


    @Test
    void signUp() {
        // API 요청 설정
        String url = "http://localhost:" + port + "/members/sign-up";
        ResponseEntity<MemberDto> responseEntity = testRestTemplate.postForEntity(url, signUpDto, MemberDto.class);

        // 응답 검증
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        MemberDto savedMemberDto = responseEntity.getBody();
        Assertions.assertThat(savedMemberDto.getUsername()).isEqualTo(signUpDto.getUsername());
        Assertions.assertThat(savedMemberDto.getNickname()).isEqualTo(signUpDto.getNickname());
    }

    @Test
    void signIn() {
        //회원가입
        memberService.signUp(signUpDto);

        SignInDto signInDto = SignInDto.builder()
                .username("member")
                .password("12345678")
                .build();

        //로그인 요청
        JwtToken jwtToken = memberService.signIn(signInDto.getUsername(), signInDto.getPassword());

        //HttpHeaders 객체 생성 및 토큰 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        log.info("httpHeaders = {}", httpHeaders);

        //API 요청 설정
        String url = "http://localhost:" + port + "/members/test";
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(url, new HttpEntity<>(httpHeaders), String.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("success");

        //Refresh Token 저장 확인
        String findRefreshToken = redisService.getValues(signInDto.getUsername());
        Assertions.assertThat(findRefreshToken).isEqualTo(jwtToken.getRefreshToken());
    }

    @Test
    void signOut() {
        //회원가입
        memberService.signUp(signUpDto);

        //로그인
        SignInDto signInDto = SignInDto.builder()
                .username("member")
                .password("12345678")
                .build();

        JwtToken jwtToken = memberService.signIn(signInDto.getUsername(), signInDto.getPassword());

        //로그아웃
        memberService.signOut(jwtToken.getRefreshToken(), jwtToken.getAccessToken());

        //HttpHeaders 객체 생성 및 토큰 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        //API 요청 설정
        String url = "http://localhost:" + port + "/members/test";
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(url, new HttpEntity<>(httpHeaders), String.class);

        log.info("http status = {}", responseEntity.getStatusCode());

        //로그아웃 후 접근이 거부되는지 확인
        //Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        //Redis에서 Refresh Token 확인 (삭제되었는지 확인)
        String findRefreshToken = redisService.getValues(signInDto.getUsername());
        log.info("refresh token = {}", findRefreshToken);
        Assertions.assertThat(findRefreshToken).isEqualTo("false");

        //Access Token이 블랙리스트에 저장되었는지 확인
        String accessTokenStatus = redisService.getValues(jwtToken.getAccessToken());
        log.info("access token = {}", accessTokenStatus);
        Assertions.assertThat(accessTokenStatus).isEqualTo("logout");

    }

    @Test
    void reissueAccessToken() throws Exception {
        //회원가입
        memberService.signUp(signUpDto);

        //로그인
        SignInDto signInDto = SignInDto.builder()
                .username("member")
                .password("12345678")
                .build();

        JwtToken jwtToken = memberService.signIn(signInDto.getUsername(), signInDto.getPassword());

        String newAccessToken = memberService.reissueAccessToken(jwtToken.getRefreshToken());

        Assertions.assertThat(newAccessToken).isNotEqualTo(jwtToken.getAccessToken());

        //HttpHeaders 객체 생성 및 토큰 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(newAccessToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        //API 요청 설정
        String url = "http://localhost:" + port + "/members/test";
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(url, new HttpEntity<>(httpHeaders), String.class);

        log.info("http status = {}", responseEntity.getStatusCode());
    }
}