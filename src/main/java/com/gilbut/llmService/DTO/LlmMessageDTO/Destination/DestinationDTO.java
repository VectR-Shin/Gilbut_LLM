package com.gilbut.llmService.DTO.LlmMessageDTO.Destination;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/*
 * 이후의 확장을 위해 유지함
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "subType",
        defaultImpl = DescribeDestinationDTO.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DescribeDestinationDTO.class, name = "DESCRIBE")
})
public sealed interface DestinationDTO permits DescribeDestinationDTO {
}
