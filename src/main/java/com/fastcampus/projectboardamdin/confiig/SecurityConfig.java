package com.fastcampus.projectboardamdin.confiig;

import com.fastcampus.projectboardamdin.domain.constant.RoleType;
import com.fastcampus.projectboardamdin.dto.security.BoardAdminPrincipal;
import com.fastcampus.projectboardamdin.dto.security.KakaoOAuth2Response;
import com.fastcampus.projectboardamdin.service.AdminAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Set;
import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        String[] rolesAboveManager = { RoleType.MANAGER.name(), RoleType.DEVELOPER.name(), RoleType.ADMIN.name() };

        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 정적 리소스 접근 허용
                        .requestMatchers(HttpMethod.POST, "/**").hasAnyRole(rolesAboveManager) // 일반 유저는 POST 요청 불가능
                        .requestMatchers(HttpMethod.DELETE, "/**").hasAnyRole(rolesAboveManager) // 일반 유저는 DELETE 요청 불가능
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .oauth2Login(withDefaults())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(AdminAccountService adminAccountService) {
        return username -> adminAccountService
                .searchUser(username)
                .map(BoardAdminPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다. - username: " + username));
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(
            AdminAccountService adminAccountService,
            PasswordEncoder passwordEncoder
    ) {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        return userRequest -> {
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            String providerId = String.valueOf(kakaoResponse.getId());
            String username = registrationId + "_" + providerId;
            String dummyPassword = passwordEncoder.encode("{bcrypt}" + UUID.randomUUID());
            Set<RoleType> roleTypes = Set.of(RoleType.USER);

            return adminAccountService.searchUser(username)
                    .map(BoardAdminPrincipal::from)
                    .orElseGet(() ->
                            BoardAdminPrincipal.from(
                                    adminAccountService.saveUser(
                                            username,
                                            dummyPassword,
                                            roleTypes,
                                            kakaoResponse.email(),
                                            kakaoResponse.nickname(),
                                            null
                                    )
                            )
                    );
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
