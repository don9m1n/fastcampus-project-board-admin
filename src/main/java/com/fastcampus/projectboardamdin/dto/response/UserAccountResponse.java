package com.fastcampus.projectboardamdin.dto.response;

import com.fastcampus.projectboardamdin.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountResponse {

    private String userId;
    private String email;
    private String nickname;
    private String memo;
    private LocalDateTime createdAt;
    private String createdBy;

    public static UserAccountResponse of(String userId, String email, String nickname, String memo, LocalDateTime createdAt, String createdBy) {
        return new UserAccountResponse(userId, email, nickname, memo, createdAt, createdBy);
    }

    public static UserAccountResponse from(UserAccountDto dto) {
        return UserAccountResponse.of(
                dto.getUserId(),
                dto.getEmail(),
                dto.getNickname(),
                dto.getMemo(),
                dto.getCreatedAt(),
                dto.getCreatedBy()
        );
    }
}
