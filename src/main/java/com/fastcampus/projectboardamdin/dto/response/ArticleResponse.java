package com.fastcampus.projectboardamdin.dto.response;

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
public class ArticleResponse {

    private Long id;
    private UserAccountDto userAccount;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public static ArticleResponse of(Long id, UserAccountDto userAccount, String title, String content, LocalDateTime createdAt) {
        return new ArticleResponse(id, userAccount, title, content, createdAt);
    }

    public static ArticleResponse withContent(ArticleDto dto) {
        return ArticleResponse.of(dto.getId(), dto.getUserAccount(), dto.getTitle(), dto.getContent(), dto.getCreatedAt());
    }

    public static ArticleResponse withoutContent(ArticleDto dto) {
        return ArticleResponse.of(dto.getId(), dto.getUserAccount(), dto.getTitle(), null, dto.getCreatedAt());
    }


}
