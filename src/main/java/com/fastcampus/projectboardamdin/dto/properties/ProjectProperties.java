package com.fastcampus.projectboardamdin.dto.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectProperties {

    private Board board;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Board {
        private String url;
    }
}
