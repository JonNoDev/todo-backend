package uk.co.powdr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.powdr.model.TodoItem;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
}
