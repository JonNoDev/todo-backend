package uk.co.powdr.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddTodoRequest {

    private String title;
    private String description;
}
