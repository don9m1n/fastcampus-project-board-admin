package com.fastcampus.projectboardamdin.controller;

import com.fastcampus.projectboardamdin.confiig.SecurityConfig;
import com.fastcampus.projectboardamdin.domain.constant.RoleType;
import com.fastcampus.projectboardamdin.dto.ArticleCommentDto;
import com.fastcampus.projectboardamdin.dto.UserAccountDto;
import com.fastcampus.projectboardamdin.service.ArticleCommentManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("컨트롤러 - 댓글 관리")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleCommentManagementController.class)
class ArticleCommentManagementControllerTest {

    private final MockMvc mvc;

    @MockBean
    private ArticleCommentManagementService articleCommentManagementService;

    public ArticleCommentManagementControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 댓글 관리 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleCommentManagementView_thenReturnsArticleCommentManagementView() throws Exception {
        // given
        given(articleCommentManagementService.getArticleComments()).willReturn(List.of());

        // when
        mvc.perform(get("/management/article-comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/articleComments"))
                .andExpect(model().attribute("comments", List.of()));

        // then
        then(articleCommentManagementService).should().getArticleComments();

    }

    @DisplayName("[data][GET] 댓글 1개 - 정상 호출")
    @Test
    void givenCommentId_whenRequestingArticleComment_thenReturnsArticleComment() throws Exception {
        // Given
        Long articleCommentId = 1L;
        ArticleCommentDto articleCommentDto = createArticleCommentDto("comment");
        given(articleCommentManagementService.getArticleComment(articleCommentId)).willReturn(articleCommentDto);

        // When & Then
        mvc.perform(get("/management/article-comments/" + articleCommentId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(articleCommentId))
                .andExpect(jsonPath("$.content").value(articleCommentDto.getContent()))
                .andExpect(jsonPath("$.userAccount.nickname").value(articleCommentDto.getUserAccount().getNickname()));

        then(articleCommentManagementService).should().getArticleComment(articleCommentId);
    }

    @DisplayName("[view][POST] 댓글 삭제 - 정상 호출")
    @Test
    void givenCommentId_whenRequestingDeletion_thenRedirectsToArticleCommentManagementView() throws Exception {
        // Given
        Long articleCommentId = 1L;
        willDoNothing().given(articleCommentManagementService).deleteArticleComment(articleCommentId);

        // When & Then
        mvc.perform(
                        post("/management/article-comments/" + articleCommentId)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/management/article-comments"))
                .andExpect(redirectedUrl("/management/article-comments"));
        then(articleCommentManagementService).should().deleteArticleComment(articleCommentId);
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