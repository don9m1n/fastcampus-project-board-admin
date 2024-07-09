package com.fastcampus.projectboardamdin.dto.response;

import com.fastcampus.projectboardamdin.dto.ArticleCommentDto;
import com.fastcampus.projectboardamdin.dto.ArticleDto;
import com.fastcampus.projectboardamdin.dto.UserAccountDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // null 인 데이터는 응답에서 제외
public class ArticleCommentResponse {

    private Long id;
    private UserAccountDto userAccount;
    private String content;
    private LocalDateTime createdAt;

    public static ArticleCommentResponse of(Long id, UserAccountDto userAccount, String content, LocalDateTime createdAt) {
        return new ArticleCommentResponse(id, userAccount, content, createdAt);
    }

    public static ArticleCommentResponse of(ArticleCommentDto dto) {
        return ArticleCommentResponse.of(dto.getId(), dto.getUserAccount(), dto.getContent(), dto.getCreatedAt());
    }


}
