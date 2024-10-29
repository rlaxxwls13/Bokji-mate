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
    private LocalDate birthDate;
    private String job;
    private String workExperience;
    private String residence;
    private List<String> interests;
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public Member toEntity(String encodedPassword, List<String> roles) {
        return Member.builder()
                .username(username)
                .password(encodedPassword)
                .nickname(nickname)
                .birthDate(birthDate)
                .job(job)
                .workExperience(workExperience)
                .residence(residence)
                .interests(interests)
                .roles(roles)
                .build();
    }

}
