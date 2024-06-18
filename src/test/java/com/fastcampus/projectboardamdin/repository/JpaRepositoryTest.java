package com.fastcampus.projectboardamdin.repository;

import com.fastcampus.projectboardamdin.domain.UserAccount;
import com.fastcampus.projectboardamdin.domain.constant.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA Repository 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest // @Transactional 포함.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaRepositoryTest {

    private final UserAccountRepository userAccountRepository;

    public JpaRepositoryTest(@Autowired UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @DisplayName("회원 정보 select 테스트")
    @Test
    void givenUserAccounts_whenSelecting_thenWorksFine() {
        // given

        // when
        List<UserAccount> userAccounts = userAccountRepository.findAll();

        // then
        assertThat(userAccounts)
                .isNotNull()
                .hasSize(4);
    }

    @DisplayName("회원 정보 insert 테스트")
    @Test
    void givenUserAccount_whenInserting_thenWorksFine() {
        // given
        long previousCount = userAccountRepository.count();
        UserAccount userAccount = UserAccount.of(
                "test",
                "pw",
                Set.of(RoleType.DEVELOPER),
                null,
                null,
                null
        );

        // when
        userAccountRepository.save(userAccount);

        // then
        assertThat(userAccountRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("회원 정보 update 테스트")
    @Test
    void givenUserAccountAndRoleType_whenUpdating_thenWorksFine() {
        // given
        UserAccount userAccount = userAccountRepository.getReferenceById("dongmin");
        userAccount.addRoleType(RoleType.DEVELOPER);
        userAccount.addRoleTypes(List.of(RoleType.USER, RoleType.USER));
        userAccount.removeRoleType(RoleType.ADMIN);

        // when
        // update query 확인을 위해 save 시점에 flush !!
        UserAccount updatedAccount = userAccountRepository.saveAndFlush(userAccount);

        // then
        assertThat(updatedAccount)
                .hasFieldOrPropertyWithValue("userId", "dongmin")
                .hasFieldOrPropertyWithValue("roleTypes", Set.of(RoleType.DEVELOPER, RoleType.USER));
    }

    @DisplayName("회원 정보 delete 테스트")
    @Test
    void givenUserAccount_whenDeleting_thenWorksFine() {
        // given
        long previousCount = userAccountRepository.count();
        UserAccount userAccount = userAccountRepository.getReferenceById("dongmin");

        // when
        userAccountRepository.delete(userAccount);

        // then
        assertThat(userAccountRepository.count()).isEqualTo(previousCount - 1);
    }

    @EnableJpaAuditing
    @TestConfiguration
    static class TestJpaConfig {

        @Bean
        AuditorAware<String> auditorAware() {
            return () -> Optional.of("dongmin");
        }
    }
}
