package com.fastcampus.projectboardamdin.service;

import com.fastcampus.projectboardamdin.dto.ArticleDto;
import com.fastcampus.projectboardamdin.dto.UserAccountDto;
import com.fastcampus.projectboardamdin.dto.properties.ProjectProperties;
import com.fastcampus.projectboardamdin.dto.response.ArticleClientResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@DisplayName("비즈니스 로직 - 게시글 관리")
class ArticleManagementServiceTest {

    @Disabled("실제 API 호출 결과를 관찰용이므로 평상시엔 비활성화")
    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealApiTest {

        private final ArticleManagementService sut;

        @Autowired
        public RealApiTest(ArticleManagementService sut) {
            this.sut = sut;
        }

        @DisplayName("게시글 API를 호출하면, 게시글을 가져온다.")
        @Test
        void givenNothing_whenCallingArticleApi_thenReturnArticleList() {
            // given


            // when
            List<ArticleDto> result = sut.getArticles();

            // then
            System.out.println(result.stream().findFirst());
            assertThat(result).isNotNull();
        }

    }

    @DisplayName("API Mocking Test")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(ArticleManagementService.class)
    @Nested
    class RestTemplateTest {

        private final ArticleManagementService sut;
        private final ProjectProperties projectProperties;
        private final MockRestServiceServer server;
        private final ObjectMapper mapper;

        @Autowired
        public RestTemplateTest(ArticleManagementService sut, ProjectProperties projectProperties, MockRestServiceServer server, ObjectMapper mapper) {
            this.sut = sut;
            this.projectProperties = projectProperties;
            this.server = server;
            this.mapper = mapper;
        }

        @DisplayName("게시글 목록 API를 호출하면, 게시글들을 가져온다.")
        @Test
        void givenNothing_whenCallingArticlesApi_thenReturnArticleList() throws JsonProcessingException {

            // given
            ArticleDto expectedArticle = createArticleDto("제목", "내용");
            ArticleClientResponse expectedResponse = ArticleClientResponse.of(List.of(expectedArticle));
            server
                    .expect(requestTo(projectProperties.getBoard().getUrl() + "/api/articles?size=10000"))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedResponse), // JSON 변환
                            MediaType.APPLICATION_JSON
                    ));

            // when
            List<ArticleDto> result = sut.getArticles();

            // then
            assertThat(result)
                    .first()
                    .hasFieldOrPropertyWithValue("id", expectedArticle.getId())
                    .hasFieldOrPropertyWithValue("title", expectedArticle.getTitle())
                    .hasFieldOrPropertyWithValue("content", expectedArticle.getContent())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedArticle.getUserAccount().getNickname());

            // 위에서 작성한 통신이 실행되었는지 검증
            server.verify();
        }

        @DisplayName("게시글 API를 호출하면, 게시글을 가져온다.")
        @Test
        void givenNothing_whenCallingArticleApi_thenReturnArticle() throws JsonProcessingException {

            // given
            Long articleId = 1L;
            ArticleDto expectedArticle = createArticleDto("게시판", "글");
            server
                    .expect(requestTo(projectProperties.getBoard().getUrl() + "/api/articles/" + articleId))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedArticle),
                            MediaType.APPLICATION_JSON
                    ));

            // when
            ArticleDto result = sut.getArticle(articleId);

            // then
            assertThat(result)
                    .hasFieldOrPropertyWithValue("id", expectedArticle.getId())
                    .hasFieldOrPropertyWithValue("title", expectedArticle.getTitle())
                    .hasFieldOrPropertyWithValue("content", expectedArticle.getContent())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedArticle.getUserAccount().getNickname());

            server.verify();
        }

        @DisplayName("게시글 ID와 함께 게시글 삭제 API를 호출하면, 게시글을 삭제한다.")
        @Test
        void givenArticleId_whenCallingDeleteArticleApi_thenDeleteArticle() throws JsonProcessingException {

            // given
            Long articleId = 1L;

            server
                    .expect(requestTo(projectProperties.getBoard().getUrl() + "/api/articles/" + articleId))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());

            // when
            sut.deleteArticle(articleId);

            // then
            server.verify();
        }

        private ArticleDto createArticleDto(String title, String content) {
            return ArticleDto.of(
                    1L,
                    createUserAccountDto(),
                    title,
                    content,
                    null,
                    LocalDateTime.now(),
                    "Uno",
                    LocalDateTime.now(),
                    "Uno"
            );
        }

        private UserAccountDto createUserAccountDto() {
            return UserAccountDto.of(
                    "unoTest",
                    "uno-test@email.com",
                    "uno-test",
                    "test memo"
            );
        }
    }

}