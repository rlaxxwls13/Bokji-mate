package mover.bokji_mate.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mover.bokji_mate.Service.MemberService;
import mover.bokji_mate.dto.JwtToken;
import mover.bokji_mate.dto.MemberDto;
import mover.bokji_mate.dto.SignInDto;
import mover.bokji_mate.dto.SignUpDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody SignInDto signInDto) {
        String username = signInDto.getUsername();
        String password = signInDto.getPassword();
        JwtToken jwtToken = memberService.signIn(username, password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<MemberDto> signUp(@RequestBody SignUpDto signUpDto) {
        MemberDto savedMemberDto = memberService.signUp(signUpDto);
        return ResponseEntity.ok(savedMemberDto);
    }

    @PostMapping("test")
    public String test() {
        return "success";
    }
}
