package org.example.recap_21_06_24.controller;

import lombok.RequiredArgsConstructor;
import org.example.recap_21_06_24.model.ToDoWithoutId;
import org.example.recap_21_06_24.model.Todo;
import org.example.recap_21_06_24.service.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public List<Todo> showAllTodos(){
        return todoService.showAllTodos();
    }

    @PostMapping
    public void addTodo(@RequestBody ToDoWithoutId todo){
        todoService.addTodo(todo);
    }

    @GetMapping("/{id}")
    public Todo getDetail(@PathVariable String id){
        return todoService.getDetail(id);
    }

    @PutMapping("/{id}")
    public void updateTodo(@PathVariable String id, @RequestBody ToDoWithoutId todo){
        todoService.updateTodo(id, todo);
    }
}
