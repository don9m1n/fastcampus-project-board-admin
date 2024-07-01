package com.fastcampus.projectboardamdin.dto.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("project")
@Getter
public class ProjectProperties {

    private Board board;

    @Getter
    public class Board {
        private String url;
    }
}
