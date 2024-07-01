package com.fastcampus.projectboardamdin.dto.response;

import com.fastcampus.projectboardamdin.dto.UserAccountDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountClientResponse {

    @JsonProperty("_embedded")
    private Embedded embedded;

    @JsonProperty("page")
    private Page page;

    public static UserAccountClientResponse empty() {
        return new UserAccountClientResponse(
                new Embedded(List.of()),
                new Page(1, 0, 1, 0)
        );
    }

    public static UserAccountClientResponse of(List<UserAccountDto> userAccounts) {
        return new UserAccountClientResponse(
                new Embedded(userAccounts),
                new Page(userAccounts.size(), userAccounts.size(), 1, 0)
        );
    }

    public List<UserAccountDto> userAccounts() { return this.getEmbedded().getUserAccounts(); }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
        private List<UserAccountDto> userAccounts;
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
