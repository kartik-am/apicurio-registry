
package io.apicurio.registry.storage.impl.sql.mappers;

import io.apicurio.registry.content.ContentHandle;
import io.apicurio.registry.storage.dto.MarkdownContentDto;
import io.apicurio.registry.storage.impl.sql.jdb.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MarkdownContentDtoRowMapper implements RowMapper<MarkdownContentDto> {

    public static final MarkdownContentDtoRowMapper instance = new MarkdownContentDtoRowMapper();

    private MarkdownContentDtoRowMapper() {
    }

    @Override
    public MarkdownContentDto map(ResultSet rs) throws SQLException {
        return MarkdownContentDto.builder()
                .content(ContentHandle.create(rs.getBytes("content")))
                .version("version")
                .build();
    }
}
