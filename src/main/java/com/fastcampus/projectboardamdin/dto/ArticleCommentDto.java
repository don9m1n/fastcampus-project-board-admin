package com.fastcampus.projectboardamdin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCommentDto {

    private Long id;
    private Long articleId;
    private UserAccountDto userAccount;
    private Long parentCommentId;
    private String content;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    public static ArticleCommentDto of(Long id, Long articleId, UserAccountDto userAccount, Long parentCommentId, String content, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new ArticleCommentDto(id, articleId, userAccount, parentCommentId, content, createdAt, createdBy, modifiedAt, modifiedBy);
    }
}
