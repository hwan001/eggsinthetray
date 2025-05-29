package dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuccessResponse {
    private boolean success;

    public static SuccessResponse of(boolean isMatch) {
        return SuccessResponse.builder()
                .success(isMatch)
                .build();
    }
} 