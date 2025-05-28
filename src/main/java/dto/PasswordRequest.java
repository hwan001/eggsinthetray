package dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordRequest {
    private String password;
}
