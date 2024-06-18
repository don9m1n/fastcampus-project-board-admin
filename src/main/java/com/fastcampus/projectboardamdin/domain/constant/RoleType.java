package com.fastcampus.projectboardamdin.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {

    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER"),
    DEVELOPER("ROLE_DEVELOPER"),
    ADMIN("ROLE_ADMIN")
    ;

    // name은 enum의 예약어이기 때문에 roleName으로 설정
    private final String roleName;

}
