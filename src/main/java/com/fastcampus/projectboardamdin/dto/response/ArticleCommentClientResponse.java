package com.fastcampus.projectboardamdin.dto.response;

import com.fastcampus.projectboardamdin.dto.ArticleCommentDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCommentClientResponse {

    @JsonProperty("_embedded")
    private Embedded embedded;

    @JsonProperty("page")
    private Page page;

    public static ArticleCommentClientResponse empty() {
        return new ArticleCommentClientResponse(
                new ArticleCommentClientResponse.Embedded(List.of()),
                new ArticleCommentClientResponse.Page(1, 0, 1, 0)
        );
    }

    public static ArticleCommentClientResponse of(List<ArticleCommentDto> articleComments) {
        return new ArticleCommentClientResponse(
                new ArticleCommentClientResponse.Embedded(articleComments),
                new ArticleCommentClientResponse.Page(articleComments.size(), articleComments.size(), 1, 0)
        );
    }

    public List<ArticleCommentDto> articleComments() { return this.getEmbedded().getArticleComments(); }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
        private List<ArticleCommentDto> articleComments;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Page {
        private int size;
        private long totalElements;
        private int totalPages;
        private int number;
    }
}
