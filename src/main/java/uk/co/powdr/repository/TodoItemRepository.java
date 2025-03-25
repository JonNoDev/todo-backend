package uk.co.powdr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.powdr.model.TodoItem;

import java.util.List;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    List<TodoItem> findAllByUserId(Long userId);
}
