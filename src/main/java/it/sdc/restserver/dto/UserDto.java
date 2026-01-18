package it.sdc.restserver.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(
        Long id,
        String name,
        List<ItemDto> items
) {
}
