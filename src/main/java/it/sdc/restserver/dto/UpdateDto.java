package it.sdc.restserver.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateDto(
        @JsonProperty("update_id") Integer updateId, @JsonProperty("message") Message message,
        @JsonProperty("edited_message") Message editedMessage,
        @JsonProperty("callback_query") CallbackQuery callbackQuery,
        @JsonProperty("my_chat_member") ChatMemberUpdated myChatMember) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Message(@JsonProperty("message_id") Integer messageId, @JsonProperty("from") User from,
                          @JsonProperty("date") Integer date, @JsonProperty("chat") Chat chat,
                          @JsonProperty("reply_to_message") Message replyToMessage, @JsonProperty("text") String text,
                          @JsonProperty("entities") List<MessageEntity> entities,
                          @JsonProperty("photo") List<PhotoSize> photo, @JsonProperty("audio") Audio audio,
                          @JsonProperty("document") Document document, @JsonProperty("video") Video video,
                          @JsonProperty("voice") Voice voice, @JsonProperty("caption") String caption,
                          @JsonProperty("sticker") Sticker sticker,
                          @JsonProperty("new_chat_members") List<User> newChatMembers,
                          @JsonProperty("left_chat_member") User leftChatMember) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record User(@JsonProperty("id") Long id, @JsonProperty("is_bot") boolean isBot,
                       @JsonProperty("first_name") String firstName, @JsonProperty("last_name") String lastName,
                       @JsonProperty("username") String username) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Chat(@JsonProperty("id") Long id, @JsonProperty("type") String type,
                       // private, group, supergroup, channel
                       @JsonProperty("title") String title, @JsonProperty("username") String username) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record MessageEntity(@JsonProperty("type") String type, @JsonProperty("offset") Integer offset,
                                @JsonProperty("length") Integer length, @JsonProperty("url") String url) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record PhotoSize(@JsonProperty("file_id") String fileId, @JsonProperty("width") Integer width,
                            @JsonProperty("height") Integer height, @JsonProperty("file_size") Long fileSize) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Audio(@JsonProperty("file_id") String fileId, @JsonProperty("duration") Integer duration,
                        @JsonProperty("title") String title, @JsonProperty("mime_type") String mimeType) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Document(@JsonProperty("file_id") String fileId, @JsonProperty("file_name") String fileName,
                           @JsonProperty("mime_type") String mimeType) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Video(@JsonProperty("file_id") String fileId, @JsonProperty("duration") Integer duration,
                        @JsonProperty("mime_type") String mimeType) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Voice(@JsonProperty("file_id") String fileId, @JsonProperty("duration") Integer duration) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Sticker(@JsonProperty("file_id") String fileId, @JsonProperty("emoji") String emoji,
                          @JsonProperty("is_animated") boolean isAnimated) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record CallbackQuery(@JsonProperty("id") String id, @JsonProperty("from") User from,
                                @JsonProperty("message") Message message, @JsonProperty("data") String data) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChatMemberUpdated(@JsonProperty("chat") Chat chat, @JsonProperty("from") User from,
                                    @JsonProperty("new_chat_member") ChatMember newChatMember) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChatMember(@JsonProperty("user") User user, @JsonProperty("status") String status) {
    }
}