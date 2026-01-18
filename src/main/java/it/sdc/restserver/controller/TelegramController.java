package it.sdc.restserver.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import it.sdc.restserver.dto.UpdateDto;
import it.sdc.restserver.dto.UserDto;
import it.sdc.restserver.service.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "telegram")
@Slf4j
@RequiredArgsConstructor
public class TelegramController {

    private final TelegramService telegramService;

    @GetMapping(value = "users")
    public List<UserDto> getAllUsers() {
        return telegramService.getAllUsers();
    }

    @PostMapping(value = "useless_bot")
    public ResponseEntity<Void> onUpdate(@RequestBody UpdateDto update) {
        try {
            telegramService.sendResponse(update);
        } catch (JsonProcessingException e) {
            log.error(e.getLocalizedMessage());
            log.error(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

}
