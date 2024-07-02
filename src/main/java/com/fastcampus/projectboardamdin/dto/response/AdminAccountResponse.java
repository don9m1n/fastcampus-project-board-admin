package com.fastcampus.projectboardamdin.dto.response;

import com.fastcampus.projectboardamdin.domain.constant.RoleType;
import com.fastcampus.projectboardamdin.dto.AdminAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminAccountResponse {

    private String userId;
    private String roleTypes;
    private String email;
    private String nickname;
    private String memo;
    private LocalDateTime createdAt;
    private String createdBy;

    public static AdminAccountResponse of(String userId, String roleTypes, String email, String nickname, String memo, LocalDateTime createdAt, String createdBy) {
        return new AdminAccountResponse(userId, roleTypes, email, nickname, memo, createdAt, createdBy);
    }

    public static AdminAccountResponse from(AdminAccountDto dto) {
        return AdminAccountResponse.of(
                dto.getUserId(),
                dto.getRoleTypes().stream()
                        .map(RoleType::getDescription)
                        .collect(Collectors.joining(", ")),
                dto.getEmail(),
                dto.getNickname(),
                dto.getMemo(),
                dto.getCreatedAt(),
                dto.getCreatedBy()
        );
    }
}
