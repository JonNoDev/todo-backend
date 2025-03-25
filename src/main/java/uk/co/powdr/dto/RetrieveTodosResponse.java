package uk.co.powdr.dto;

import lombok.Builder;
import lombok.Data;
import uk.co.powdr.model.TodoItem;

import java.util.List;

@Data
@Builder
public class RetrieveTodosResponse {

    private List<TodoItem> todos;
}
