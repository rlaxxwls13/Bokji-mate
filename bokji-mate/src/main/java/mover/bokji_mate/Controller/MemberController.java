package mover.bokji_mate.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mover.bokji_mate.Service.MemberService;
import mover.bokji_mate.dto.JwtToken;
import mover.bokji_mate.dto.MemberDto;
import mover.bokji_mate.dto.SignInDto;
import mover.bokji_mate.dto.SignUpDto;
import mover.bokji_mate.jwt.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/sign-up")
    public ResponseEntity<MemberDto> signUp(@RequestBody SignUpDto signUpDto) {
        MemberDto savedMemberDto = memberService.signUp(signUpDto);
        return ResponseEntity.ok(savedMemberDto);
    }

    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody SignInDto signInDto) {
        String username = signInDto.getUsername();
        String password = signInDto.getPassword();
        JwtToken jwtToken = memberService.signIn(username, password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

    @PostMapping("/sign-out")
    public ResponseEntity signOut(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        memberService.signOut(refreshToken, accessToken);


        return ResponseEntity.ok("signed out successfully.");
    }

    @PostMapping("/delete-account")
    public void deleteAccount() {

    }

    @PatchMapping("/reissue")
    public ResponseEntity reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        String newAccessToken = memberService.reissueAccessToken(refreshToken);
        jwtTokenProvider.accessTokenSetHeader(newAccessToken, response);

        return ResponseEntity.ok("The access token was successfully reissued.");
    }

    @GetMapping("/{username}")
    public void getMemberInfo() {

    }

    @PutMapping("/{username}")
    public void updateMemberInfo() {

    }

    @PostMapping("test")
    public String test() {
        return "success";
    }
}
