package org.example.recap_21_06_24.controller;

import org.example.recap_21_06_24.model.Status;
import org.example.recap_21_06_24.model.ToDoWithoutId;
import org.example.recap_21_06_24.model.Todo;
import org.example.recap_21_06_24.repository.TodoRepository;
import org.example.recap_21_06_24.service.IdService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private IdService idService;

    @BeforeEach
    void setup(){
        todoRepository.deleteAll();
    }

    @Test
    void showAllTodos_whenCalledInitially_shouldReturnEmptyList() throws Exception {
        mockMvc.perform((MockMvcRequestBuilders.get("/api/todo")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));

    }

    @Test
    void addTodo_whenCalledWithToDoWithoutId_shouldGenerateIdAndSaveTodoObjectInDB() throws Exception {

        String id = idService.idGenerator();
        ToDoWithoutId toDoWithoutId = new ToDoWithoutId("Einkaufen", Status.OPEN);
        todoRepository.save(new Todo(id, toDoWithoutId.description(), toDoWithoutId.status()));

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                
                                {
                                "description": "Einkaufen",
                                "status": "OPEN"
                                }
                                
                                """))
                .andExpect(status().isOk());

    }

    @Test
    void getDetail_whenCalledWithId123_shouldReturnTodoObjectWithId123() throws Exception {
        Todo savedTodo = new Todo("123", "Einkaufen", Status.OPEN);
        todoRepository.save(savedTodo);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/{id}", "123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedTodo.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(savedTodo.description()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(savedTodo.status().toString()));
    }

    @Test
    void updateTodo() {
    }

    @Test
    void deleteTodo() throws Exception {
        Todo savedTodo = new Todo("123", "Einkaufen", Status.OPEN);
        todoRepository.save(savedTodo);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/{id}", "123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }
}