package com.fastcampus.projectboardamdin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

    private Long id;
    private UserAccountDto userAccount;
    private String title;
    private String content;
    private Set<String> hashtags;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    public static ArticleDto of(Long id, UserAccountDto userAccount, String title, String content, Set<String> hashtags, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new ArticleDto(id, userAccount, title, content, hashtags, createdAt, createdBy, modifiedAt, modifiedBy);
    }
}
