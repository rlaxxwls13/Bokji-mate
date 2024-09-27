package mover.bokji_mate.Service;

import mover.bokji_mate.domain.Member;
import mover.bokji_mate.dto.MemberDto;
import mover.bokji_mate.dto.SignUpDto;
import mover.bokji_mate.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;


    @Test
    void signUp() throws Exception {
        //given
        SignUpDto signUpDto = SignUpDto.builder()
                .username("member")
                .password("12345678")
                .nickname("김하진")
                .build();

        //when
        Member joinedMember = memberService.signUp(signUpDto).toEntity();

        //then
        Optional<Member> findMember = memberRepository.findById(joinedMember.getId());
        assertTrue(findMember.isPresent());
        assertEquals(joinedMember, findMember.get());
    }

    @Test()
    void 중복_아이디_검증() throws Exception {
        //given
        SignUpDto signUpDto1 = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("김하진")
                .build();

        SignUpDto signUpDto2 = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("김하진")
                .build();

        //when
        memberService.signUp(signUpDto1);
        //memberService.signUp(signUpDto2);

        //then
        assertThrows(IllegalStateException.class, () -> {
            memberService.signUp(signUpDto2);
        });
    }
}