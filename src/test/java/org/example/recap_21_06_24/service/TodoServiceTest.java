package org.example.recap_21_06_24.service;

import org.example.recap_21_06_24.model.Status;
import org.example.recap_21_06_24.model.ToDoWithoutId;
import org.example.recap_21_06_24.model.Todo;
import org.example.recap_21_06_24.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {
    private final TodoRepository mockTodoRepository = mock(TodoRepository.class);
    private final IdService idService = new IdService();
    private final TodoService todoService = new TodoService(mockTodoRepository, idService);

    @Test
    void addTodo() {
        // Verhalten vorgeben
        ToDoWithoutId todoToDoWithoutId = new ToDoWithoutId("Einkaufen", Status.OPEN);
        Todo expectedTodo = new Todo(idService.idGenerator(), "Einkaufen", Status.OPEN);

        // Die Methode wird getestet
        todoService.addTodo(todoToDoWithoutId);

        // Auswertung
        ArgumentCaptor<Todo> todoArgumentCaptor = ArgumentCaptor.forClass(Todo.class);
        verify(mockTodoRepository, times(1)).save(todoArgumentCaptor.capture());
        Todo savedTodo = todoArgumentCaptor.getValue();

        assertNotNull(savedTodo.id());
        assertEquals(expectedTodo.description(), savedTodo.description());
        assertEquals(expectedTodo.status(), savedTodo.status());
    }

    @Test
    void showAllTodos() {
        List<Todo> expectedTodoList = List.of(new Todo(idService.idGenerator(), "Einkaufen", Status.OPEN),
                new Todo(idService.idGenerator(), "Aufräumen", Status.OPEN));

        when(mockTodoRepository.findAll()).thenReturn(expectedTodoList);

        List<Todo> actualTodoList = todoService.showAllTodos();

        verify(mockTodoRepository, times(1)).findAll();
        assertEquals(expectedTodoList, actualTodoList);
    }

    @Test
    void getDetail() {
        String id = idService.idGenerator();
        Optional<Todo> expected = Optional.of(new Todo(id, "Einkaufen", Status.OPEN));

        when(mockTodoRepository.findById(id)).thenReturn(expected);

        Todo actual = todoService.getDetail(id);

        verify(mockTodoRepository, times(1)).findById(id);
        assertEquals(expected.orElseThrow().description(), actual.description());
        assertEquals(expected.orElseThrow().status(), actual.status());
        assertEquals(expected.orElseThrow().id(), actual.id());
    }

    @Test
    void updateTodo() {
        String id = idService.idGenerator();
        Todo expected = new Todo(id, "Einkaufen", Status.OPEN);
        ToDoWithoutId todoToDoWithoutId = new ToDoWithoutId("Einkaufen", Status.IN_PROGRESS);
        when(mockTodoRepository.existsById(id)).thenReturn(true);
        when(mockTodoRepository.findById(id)).thenReturn(Optional.of(expected));

        todoService.updateTodo(id, todoToDoWithoutId);

        verify(mockTodoRepository, times(1)).findById(id);
        verify(mockTodoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void deleteTodo() {
        String id = idService.idGenerator();
        Todo todoToDelete = new Todo(id, "Einkaufen", Status.OPEN);
        Optional<Todo> expected = Optional.of(todoToDelete);

        when(mockTodoRepository.findById(id)).thenReturn(expected);

        todoService.deleteTodo(id);

        verify(mockTodoRepository, times(1)).findById(id);
        verify(mockTodoRepository, times(1)).delete(todoToDelete);
    }
}