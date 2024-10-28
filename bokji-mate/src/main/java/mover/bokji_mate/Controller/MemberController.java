package mover.bokji_mate.Controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
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

    @PostMapping("/sign-up/validate-username")
    public ResponseEntity<String> validateUsername(@RequestParam String username) {
        memberService.validateDuplicateMember(username);
        return ResponseEntity.ok(username);
    }

    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody SignInDto signInDto, HttpServletResponse response) {
        String username = signInDto.getUsername();
        String password = signInDto.getPassword();
        JwtToken jwtToken = memberService.signIn(username, password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());

        // 쿠키에 refresh token 저장
        Cookie refreshTokenCookie = new Cookie("refreshToken", jwtToken.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(refreshTokenCookie);

        return jwtToken;
    }

    @PostMapping("/sign-out")
    public ResponseEntity signOut(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        memberService.signOut(accessToken);

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

    @GetMapping("/edit")
    public ResponseEntity<MemberDto> getMemberInfo(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        MemberDto memberDto = memberService.getMemberDto(accessToken);

        return ResponseEntity.ok(memberDto);
    }

    @PutMapping("/edit")
    public ResponseEntity<String> updateMemberInfo(@RequestBody MemberDto memberDto) {
        memberService.updateProfile(memberDto);
        return ResponseEntity.ok("Member updated successfully");
    }

    @PostMapping("test")
    public String test() {
        return "success";
    }
}
