package uk.co.powdr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.powdr.dto.*;
import uk.co.powdr.service.TodoService;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Tag(name = "Todos", description = "Endpoints for managing todo-items")
public class TodoController {

    private final TodoService todoService;

    @Operation(summary = "Add a new to-do item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added to-do item",
                    content = @Content(schema = @Schema(implementation = AddTodoResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<AddTodoResponse> addTodo(@RequestBody AddTodoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.addTodo(request));
    }

    @Operation(summary = "Add a new to-do as an admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added to-do item",
                    content = @Content(schema = @Schema(implementation = AddTodoResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @PostMapping("/{userId}")
    public ResponseEntity<AddTodoResponse> addTodo(@PathVariable Long userId, @RequestBody AddTodoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.addTodo(userId, request));
    }

    @Operation(summary = "Edit an existing to-do item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully edited to-do item",
                    content = @Content(schema = @Schema(implementation = EditTodoResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "To-do item not found",
                    content = @Content)
    })
    @PutMapping("/{todoId}")
    public ResponseEntity<EditTodoResponse> editTodo(@PathVariable Long todoId, @RequestBody EditTodoRequest request) {
        return ResponseEntity.ok(todoService.editTodo(todoId, request));
    }

    @Operation(summary = "Delete an existing to-do item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted to-do item",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "To-do item not found",
                    content = @Content)
    })
    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long todoId) {
        todoService.deleteTodo(todoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retrieve existing to-do items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved to-do items",
                    content = @Content(schema = @Schema(implementation = AddTodoResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<RetrieveTodosResponse> retrieveTodos() {
        return ResponseEntity.ok(todoService.retrieveTodos());
    }

    @Operation(summary = "Retrieve existing to-do items as an admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved to-do items",
                    content = @Content(schema = @Schema(implementation = AddTodoResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/{userId}")
    public ResponseEntity<RetrieveTodosResponse> retrieveTodos(@PathVariable Long userId) {
        return ResponseEntity.ok(todoService.retrieveTodos(userId));
    }
}
