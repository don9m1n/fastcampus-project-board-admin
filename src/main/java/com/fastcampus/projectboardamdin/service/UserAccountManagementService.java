package com.fastcampus.projectboardamdin.service;

import com.fastcampus.projectboardamdin.dto.UserAccountDto;
import com.fastcampus.projectboardamdin.dto.properties.ProjectProperties;
import com.fastcampus.projectboardamdin.dto.response.UserAccountClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAccountManagementService {

    private final RestTemplate restTemplate;
    private final ProjectProperties projectProperties;

    @Transactional(readOnly = true)
    public List<UserAccountDto> getUserAccounts() {

        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.getBoard().getUrl() + "/api/userAccounts")
                .queryParam("size", 10000) // TODO: 전체 회원 목록을 가져오기 위해 충분히 큰 사이즈를 전달하는 방식. 불완전하다.
                .build()
                .toUri();

        UserAccountClientResponse response = restTemplate.getForObject(uri, UserAccountClientResponse.class);

        return Optional.ofNullable(response).orElseGet(UserAccountClientResponse::empty).userAccounts();
    }

    @Transactional(readOnly = true)
    public UserAccountDto getUserAccount(String userId) {

        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.getBoard().getUrl() + "/api/userAccounts/" + userId)
                .build()
                .toUri();
        UserAccountDto response = restTemplate.getForObject(uri, UserAccountDto.class);

        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("게시글이 없습니다 - userId: " + userId));
    }

    public void deleteUserAccount(String userId) {

        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.getBoard().getUrl() + "/api/userAccounts/" + userId)
                .build()
                .toUri();

        restTemplate.delete(uri);
    }

}
