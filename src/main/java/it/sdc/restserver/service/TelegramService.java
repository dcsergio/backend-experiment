package it.sdc.restserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.sdc.restserver.Jokes;
import it.sdc.restserver.dto.TelegramSendMessageRequestDto;
import it.sdc.restserver.dto.UpdateDto;
import it.sdc.restserver.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final Jokes jokes;

    @Value("${telegram.botUrl}")
    private String botUrl;

    @Value("${telegram.botName}")
    private String botName;

    public List<UserDto> getAllUsers() {
        var users = userRepository.findAll();
        return objectMapper.convertValue(users,
                objectMapper.getTypeFactory().constructCollectionType(List.class, UserDto.class));
    }

    public void sendResponse(UpdateDto update) throws JsonProcessingException {

        log.info("""
                incoming json:
                {}
                """, objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(update));
        String inputText;

        UpdateDto.Message message = update.message();

        if (update.callbackQuery() != null) {
            inputText = update.callbackQuery().data();
        } else if (message != null) {
            if (message.leftChatMember() != null && message.leftChatMember().username().equals(botName)) {
                // they kicked me out
                return;
            }
            if (message.text() != null) {

                inputText = message.text();
            } else if (message.sticker() != null) {
                inputText = "sticker";
            } else {
                inputText = "no text";
            }
        } else {
            inputText = "no message";
        }

        if (message != null) {
            Long chatId = message.chat().id();

            var builder = TelegramSendMessageRequestDto.builder().chatId(String.valueOf(chatId));
            builder = createResponseText(inputText, builder);
            TelegramSendMessageRequestDto requestBody = builder.build();

            log.info("""
                    outgoing json:
                    {}
                    """, objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBody));
            sendMessage(requestBody);
        }
    }

    private TelegramSendMessageRequestDto.TelegramSendMessageRequestDtoBuilder createResponseText(String originalText,
            TelegramSendMessageRequestDto.TelegramSendMessageRequestDtoBuilder builder) {
        return switch (originalText.toLowerCase()) {
            case "/barzelletta" -> createJokeText(builder);
            default -> builder.text("non ho capito");
        };
    }

    private TelegramSendMessageRequestDto.TelegramSendMessageRequestDtoBuilder createJokeText(
            TelegramSendMessageRequestDto.TelegramSendMessageRequestDtoBuilder builder) {
        var joke = jokes.getRandomJoke();
        String question = joke.getQuestion();
        String answer = joke.getAnswer();
        String safeQuestion = question.replace("?", "\\?").replace(".", "\\.").replace("!", "\\!");
        String safeAnswer = answer.replace("?", "\\?").replace(".", "\\.").replace("!", "\\!");

        return builder.text("*" + safeQuestion + "*\n\n" + "||" + safeAnswer + "||").parseMode("MarkdownV2");
    }

    private void sendMessage(TelegramSendMessageRequestDto body) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TelegramSendMessageRequestDto> entity = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(Objects.requireNonNull(botUrl), entity, String.class);
    }
}
