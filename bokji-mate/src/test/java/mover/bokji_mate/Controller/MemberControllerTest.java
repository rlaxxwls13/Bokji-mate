package mover.bokji_mate.Controller;

import lombok.extern.slf4j.Slf4j;
import mover.bokji_mate.Service.MemberService;
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

    }
}