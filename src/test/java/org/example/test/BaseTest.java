package org.example.test;

import okhttp3.Credentials;
import org.example.Todo;
import org.example.TodoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.example.RandomTodoGenerator.generateRandomTodo;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    protected TodoService todoService;

    private final static String URL = "http://5.129.198.140:8080";
    protected String auth = Credentials.basic("admin", "admin");

    @BeforeAll
    void setUpRetrofit() {
        todoService = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(TodoService.class);
    }

    protected List<Todo> fillDBWithRandomTodos(Integer number){
        List<Todo> todos = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            todos.add(generateRandomTodo());
        }
        todos.forEach(s -> {
            try {
                todoService.postTodo(s).execute();
            } catch (IOException e) {
                throw new RuntimeException("Ошибка соединения с БД" + e.getMessage());
            }
        });
        return todos;
    }

    protected void deleteAllTodos() throws IOException {
        Objects.requireNonNull(todoService.getTodos().execute()
                .body()).forEach(s-> {
            try {
                todoService.deleteTodo(s.getId(),auth).execute();
            } catch (IOException e) {
                throw new RuntimeException("Ошибка соединения с БД" + e.getMessage());
            }
        });
    }
}
