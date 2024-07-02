package io.apicurio.registry.storage.dto;

import io.apicurio.registry.content.ContentHandle;
import lombok.*;

/**
 * Represents a markdown content.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class MarkdownContentDto {

    private ContentHandle content;
    private String name;

    public MarkdownContentDto() {
    }
}