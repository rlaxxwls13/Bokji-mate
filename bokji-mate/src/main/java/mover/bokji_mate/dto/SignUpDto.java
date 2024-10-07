package mover.bokji_mate.dto;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mover.bokji_mate.domain.Member;

import java.time.LocalDate;
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
    private String phoneNumber;
    private LocalDate birthDate;
    private List<String> interests;
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public Member toEntity(String encodedPassword, List<String> roles) {
        return Member.builder()
                .username(username)
                .password(encodedPassword)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .birthDate(birthDate)
                .interests(interests)
                .roles(roles)
                .build();
    }

}
