package mover.bokji_mate.dto;

import lombok.*;
import mover.bokji_mate.domain.Member;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

    private Long id;
    private String username;
    private String nickname;

    static public MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .username(username)
                .nickname(nickname)
                .build();
    }
}
