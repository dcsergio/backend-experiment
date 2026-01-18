package it.sdc.restserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ItemDto(
        Long id,
        String name
) {
}
