package org.example.stepdefinitions;

import io.cucumber.java.After;
import org.example.model.Todo;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.example.util.RandomTodoGenerator.generateRandomTodo;
import static org.example.util.RestAssuredHelper.deleteAll;
import static org.example.util.RestAssuredHelper.postTodo;

public class Hooks {

    public static void fillDBWithEntitiesBase(Integer n, List<Todo> testTodos) {
        for (int i = 0; i < n; i++) {
            testTodos.add(generateRandomTodo());
        }
        testTodos.forEach(s -> postTodo(s, SC_CREATED));
    }

    @After("@cleanDB")
    public void tearDown() {
        deleteAll();
    }
}
