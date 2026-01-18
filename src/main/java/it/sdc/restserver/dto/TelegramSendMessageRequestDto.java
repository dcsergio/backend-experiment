package it.sdc.restserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record TelegramSendMessageRequestDto(
        @JsonProperty("chat_id")
        String chatId,

        @JsonProperty("text")
        String text,

        @JsonProperty("reply_markup")
        ReplyMarkup replyMarkup,

        @JsonProperty("method")
        String method,

        @JsonProperty(value = "parse_mode", defaultValue = "MarkdownV2")
        String parseMode
) {
    @Builder
    public record ReplyMarkup(
            @JsonProperty("inline_keyboard")
            List<List<InlineKeyboardButton>> inlineKeyboard
    ) {
    }

    @Builder
    public record InlineKeyboardButton(
            @JsonProperty("text")
            String text,

            @JsonProperty("callback_data")
            String callbackData
    ) {
    }
}