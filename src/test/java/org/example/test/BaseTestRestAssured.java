package org.example.test;

import io.restassured.RestAssured;
import org.example.model.Todo;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseTestRestAssured {

    private Todo testTodo;

    @BeforeAll
    protected static void setUp(){
        RestAssured.baseURI="http://5.129.198.140:8080";
    }
}
