package com.fastcampus.projectboardamdin.service;

import com.fastcampus.projectboardamdin.dto.ArticleDto;
import com.fastcampus.projectboardamdin.dto.properties.ProjectProperties;
import com.fastcampus.projectboardamdin.dto.response.ArticleClientResponse;
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
@Transactional
@RequiredArgsConstructor
public class ArticleManagementService {

    private final RestTemplate restTemplate;
    private final ProjectProperties projectProperties;

    @Transactional(readOnly = true)
    public List<ArticleDto> getArticles() {

        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.getBoard().getUrl() + "/api/articles")
                // TODO: 페이징 기능이 필요없어서 10000개로 잡았지만, 사실은 불완전한 기능이기 때문에 이후에 수정이 필요함.
                .queryParam("size", 10000)
                .build()
                .toUri();

        ArticleClientResponse response = restTemplate.getForObject(uri, ArticleClientResponse.class);

        return Optional.ofNullable(response).orElseGet(ArticleClientResponse::empty).articles();
    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId) {

        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.getBoard().getUrl() + "/api/articles/" + articleId)
                .build()
                .toUri();

        ArticleDto response = restTemplate.getForObject(uri, ArticleDto.class);

        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("%d번 게시글이 존재하지 않습니다.".formatted(articleId)));
    }

    public void deleteArticle(Long articleId) {

        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.getBoard().getUrl() + "/api/articles/" + articleId)
                .build()
                .toUri();

        restTemplate.delete(uri);
    }
}
