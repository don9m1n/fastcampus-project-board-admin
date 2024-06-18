package com.fastcampus.projectboardamdin.repository;

import com.fastcampus.projectboardamdin.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
}
