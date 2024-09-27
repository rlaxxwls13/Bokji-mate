package mover.bokji_mate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mover.bokji_mate.domain.Member;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {
    private String username;
    private String password;
    private String nickname;
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public Member toEntity(String encodedPassword, List<String> roles) {
        return Member.builder()
                .username(username)
                .password(encodedPassword)
                .nickname(nickname)
                .roles(roles)
                .build();
    }

}
