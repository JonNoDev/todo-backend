package uk.co.powdr.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddTodoResponse {

    private Long todoAdded;
}
