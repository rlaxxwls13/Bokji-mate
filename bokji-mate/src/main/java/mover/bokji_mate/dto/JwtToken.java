package mover.bokji_mate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtToken {
    private String grantType;   //JWT에 대한 인증 타입 (Bearer 인증 방식 사용)
    private String accessToken;
    private String refreshToken;
}
