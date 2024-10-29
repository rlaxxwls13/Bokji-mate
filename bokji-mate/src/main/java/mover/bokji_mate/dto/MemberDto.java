package mover.bokji_mate.dto;

import lombok.*;
import mover.bokji_mate.domain.Member;

import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

    private Long id;
    private String username;
    private String nickname;
    private LocalDate birthDate;
    private String job;
    private String workExperience;
    private String residence;
    private List<String> interests;

    static public MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .birthDate(member.getBirthDate())
                .job(member.getJob())
                .workExperience(member.getWorkExperience())
                .residence(member.getResidence())
                .interests(member.getInterests())
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .username(username)
                .nickname(nickname)
                .birthDate(birthDate)
                .interests(interests)
                .build();
    }
}
