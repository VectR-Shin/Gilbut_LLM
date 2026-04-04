package com.gilbut.llmService.DTO.LlmMessageDTO;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NavigationDTO.class, name = "NAVIGATION"),
        @JsonSubTypes.Type(value = ChatDTO.class, name = "CHAT"),
        @JsonSubTypes.Type(value = ErrorDTO.class, name = "ERROR")
})
public sealed interface LlmMessageDTO permits NavigationDTO, ChatDTO, ErrorDTO {
    LlmStatusType getType();
}
