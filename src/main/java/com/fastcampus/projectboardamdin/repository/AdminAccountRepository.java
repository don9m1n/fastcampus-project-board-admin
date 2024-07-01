package com.fastcampus.projectboardamdin.repository;

import com.fastcampus.projectboardamdin.domain.AdminAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminAccountRepository extends JpaRepository<AdminAccount, String> {
}
