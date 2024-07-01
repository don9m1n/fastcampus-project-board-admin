package com.fastcampus.projectboardamdin.service;

import com.fastcampus.projectboardamdin.domain.constant.RoleType;
import com.fastcampus.projectboardamdin.dto.ArticleCommentDto;
import com.fastcampus.projectboardamdin.dto.ArticleDto;
import com.fastcampus.projectboardamdin.dto.UserAccountDto;
import com.fastcampus.projectboardamdin.dto.properties.ProjectProperties;
import com.fastcampus.projectboardamdin.dto.response.ArticleCommentClientResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@DisplayName("비즈니스 로직 - 게시글 관리")
class ArticleManagementServiceTest {

    //@Disabled("실제 API 호출 결과를 관찰용이므로 평상시엔 비활성화")
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

    @DisplayName("API mocking 테스트")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(ArticleCommentManagementService.class)
    @Nested
    class RestTemplateTest {

        private final ArticleCommentManagementService sut;
        private final ProjectProperties projectProperties;
        private final MockRestServiceServer server;
        private final ObjectMapper mapper;

        @Autowired
        public RestTemplateTest(ArticleCommentManagementService sut, ProjectProperties projectProperties, MockRestServiceServer server, ObjectMapper mapper) {
            this.sut = sut;
            this.projectProperties = projectProperties;
            this.server = server;
            this.mapper = mapper;
        }

        @DisplayName("댓글 목록 API을 호출하면, 댓글들을 가져온다.")
        @Test
        void givenNothing_whenCallingCommentsApi_thenReturnsCommentList() throws Exception {
            // Given
            ArticleCommentDto expectedComment = createArticleCommentDto("댓글");
            ArticleCommentClientResponse expectedResponse = ArticleCommentClientResponse.of(List.of(expectedComment));

            server
                    .expect(requestTo(projectProperties.getBoard().getUrl() + "/api/articleComments?size=10000"))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));


            // When
            List<ArticleCommentDto> result = sut.getArticleComments();

            // Then
            assertThat(result).first()
                    .hasFieldOrPropertyWithValue("id", expectedComment.getId())
                    .hasFieldOrPropertyWithValue("content", expectedComment.getContent())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedComment.getUserAccount().getNickname());

            server.verify();
        }

        @DisplayName("댓글 ID와 함께 댓글 API을 호출하면, 댓글을 가져온다.")
        @Test
        void givenCommentId_whenCallingCommentApi_thenReturnsComment() throws Exception {

            // Given
            Long articleCommentId = 1L;
            ArticleCommentDto expectedComment = createArticleCommentDto("댓글");
            server
                    .expect(requestTo(projectProperties.getBoard().getUrl() + "/api/articleComments/" + articleCommentId))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedComment),
                            MediaType.APPLICATION_JSON
                    ));

            // When
            ArticleCommentDto result = sut.getArticleComment(articleCommentId);

            // Then
            assertThat(result)
                    .hasFieldOrPropertyWithValue("id", expectedComment.getId())
                    .hasFieldOrPropertyWithValue("content", expectedComment.getContent())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedComment.getUserAccount().getNickname());
            server.verify();
        }

        @DisplayName("댓글 ID와 함께 댓글 삭제 API을 호출하면, 댓글을 삭제한다.")
        @Test
        void givenCommentId_whenCallingDeleteCommentApi_thenDeletesComment() throws Exception {
            // Given
            Long articleCommentId = 1L;
            server
                    .expect(requestTo(projectProperties.getBoard().getUrl() + "/api/articleComments/" + articleCommentId))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());

            // When
            sut.deleteArticleComment(articleCommentId);

            // Then
            server.verify();
        }
    }


    private ArticleCommentDto createArticleCommentDto(String content) {
        return ArticleCommentDto.of(
                1L,
                1L,
                createUserAccountDto(),
                null,
                content,
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