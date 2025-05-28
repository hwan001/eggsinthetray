package dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordMatchResponse {
    private boolean success;

    public static PasswordMatchResponse of(boolean isMatch) {
        return PasswordMatchResponse.builder()
                .success(isMatch)
                .build();
    }
} 