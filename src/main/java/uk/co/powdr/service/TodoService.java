package uk.co.powdr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.co.powdr.dto.*;
import uk.co.powdr.exception.ResourceNotFoundException;
import uk.co.powdr.model.TodoItem;
import uk.co.powdr.repository.TodoItemRepository;
import uk.co.powdr.security.RoleValidator;

import java.util.List;

import static uk.co.powdr.security.RoleValidator.ROLE_ADMIN;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final TodoItemRepository todoItemRepository;
    private final RoleValidator roleValidator;

    public AddTodoResponse addTodo(AddTodoRequest request) {
        log.info("Creating a todo");
        Long userId = Long.valueOf(roleValidator.getSubject());

        TodoItem todoItem = TodoItem.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(false)
                .userId(userId)
                .build();
        Long todoItemId = todoItemRepository.save(todoItem).getId();
        return AddTodoResponse.builder().todoAdded(todoItemId).build();
    }

    public AddTodoResponse addTodo(Long userId, AddTodoRequest request) {
        log.info("Creating a todo for user: {}", userId);
        roleValidator.validateRole(ROLE_ADMIN);

        TodoItem todoItem = TodoItem.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(false)
                .userId(userId)
                .build();
        Long todoItemId = todoItemRepository.save(todoItem).getId();
        return AddTodoResponse.builder().todoAdded(todoItemId).build();
    }

    public EditTodoResponse editTodo(Long todoId, EditTodoRequest request) {
        log.info("Editing a todo with ID: {}", todoId);
        TodoItem todoItem = todoItemRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("No todo item with this ID can be found."));
        roleValidator.validateAdminOrUser(todoItem.getUserId().toString());
        todoItem.setTitle(request.getTitle());
        todoItem.setDescription(request.getDescription());
        todoItem.setCompleted(request.isCompleted());
        todoItemRepository.save(todoItem);
        return EditTodoResponse.builder().todoEdited(todoId).build();
    }

    public void deleteTodo(Long todoId) {
        log.info("Deleting a todo with ID: {}", todoId);
        TodoItem todoItem = todoItemRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("No todo item with this ID can be found."));
        roleValidator.validateAdminOrUser(todoItem.getUserId().toString());
        todoItemRepository.deleteById(todoItem.getId());
    }

    public RetrieveTodosResponse retrieveTodos() {
        log.info("Retrieving list of todos");
        Long userId = Long.valueOf(roleValidator.getSubject());
        List<TodoItem> todoItems = todoItemRepository.findAllByUserId(userId);
        return RetrieveTodosResponse.builder().todos(todoItems).build();
    }

    public RetrieveTodosResponse retrieveTodos(Long userId) {
        log.info("Retrieving list of todos for user: {}", userId);
        roleValidator.validateRole(ROLE_ADMIN);
        List<TodoItem> todoItems = todoItemRepository.findAllByUserId(userId);
        return RetrieveTodosResponse.builder().todos(todoItems).build();
    }

    public TodoItem retrieveTodo(Long todoId) {
        log.info("Retrieving a todo: {}", todoId);
        TodoItem todoItem = todoItemRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("No todo with this ID can be found."));
        roleValidator.validateAdminOrUser(todoItem.getUserId().toString());
        return todoItem;
    }
}
