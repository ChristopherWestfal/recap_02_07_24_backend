package org.example.recap_21_06_24.service;

import lombok.RequiredArgsConstructor;
import org.example.recap_21_06_24.model.Status;
import org.example.recap_21_06_24.model.ToDoWithoutId;
import org.example.recap_21_06_24.model.Todo;
import org.example.recap_21_06_24.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

    public final TodoRepository todoRepository;
    public final IdService idService;

    public void addTodo(ToDoWithoutId todo) {
        Todo newTodo = new Todo(idService.idGenerator(), todo.description(), Status.OPEN);
        todoRepository.save(newTodo);
    }

    public List<Todo> showAllTodos() {
        return todoRepository.findAll();
    }

    public Todo getDetail(String id){
        Optional<Todo> foundTodo = todoRepository.findById(id);
        return foundTodo.orElseThrow();
    }

    public void updateTodo(String id, ToDoWithoutId todo) {
        if(todoRepository.existsById(id)) {
            Todo foundTodo;
            if(todo.status().getStatusName().equals("IN_PROGRESS")) {
                foundTodo = todoRepository.findById(id).orElseThrow().withDescription(todo.description()).withStatus(Status.IN_PROGRESS);
                todoRepository.save(foundTodo);
            }
            else if (todo.status().getStatusName().equals("DONE")) {
                foundTodo = todoRepository.findById(id).orElseThrow().withDescription(todo.description()).withStatus(Status.DONE);
                todoRepository.save(foundTodo);
            }
        }
    }

    public void deleteTodo(String id) {
        if(todoRepository.existsById(id)){
            todoRepository.delete(todoRepository.findById(id).orElseThrow());
        }
    }
}
