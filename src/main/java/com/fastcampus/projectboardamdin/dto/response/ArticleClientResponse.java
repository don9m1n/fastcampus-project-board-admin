package com.fastcampus.projectboardamdin.dto.response;

import com.fastcampus.projectboardamdin.dto.ArticleDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleClientResponse {

    @JsonProperty("_embedded")
    private Embedded embedded;

    @JsonProperty("page")
    private Page page;

    public static ArticleClientResponse empty() {
        return new ArticleClientResponse(
                new Embedded(List.of()),
                new Page(1, 0, 1, 0)
        );
    }

    public static ArticleClientResponse of(List<ArticleDto> articles) {
        return new ArticleClientResponse(
                new Embedded(articles),
                new Page(articles.size(), articles.size(), 1, 0)
        );
    }

    public List<ArticleDto> articles() { return this.getEmbedded().getArticles(); }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
        private List<ArticleDto> articles;
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
