package com.fastcampus.projectboardamdin.dto;

import com.fastcampus.projectboardamdin.domain.AdminAccount;
import com.fastcampus.projectboardamdin.domain.constant.RoleType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAccountDto {

    private String userId;
    private Set<RoleType> roleTypes;
    private String email;
    private String nickname;
    private String memo;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    public static UserAccountDto of(String userId, Set<RoleType> roleTypes, String email, String nickname, String memo) {
        return UserAccountDto.of(userId, roleTypes, email, nickname, memo, null, null, null, null);
    }

    public static UserAccountDto of(
            String userId, Set<RoleType> roleTypes, String email,
            String nickname, String memo, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new UserAccountDto(userId, roleTypes, email, nickname, memo, createdAt, createdBy, modifiedAt, modifiedBy);
    }

}
