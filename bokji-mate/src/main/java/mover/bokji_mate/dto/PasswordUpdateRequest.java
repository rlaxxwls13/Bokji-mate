package mover.bokji_mate.dto;

import lombok.Getter;

@Getter
public class PasswordUpdateRequest {
    String currentPassword;
    String updatePassword;
}
