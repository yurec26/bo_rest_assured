package org.example.test;

import io.restassured.common.mapper.TypeRef;
import org.example.annotation.TodoParam;
import org.example.extension.TodoParamResolver;
import org.example.model.Todo;
import org.example.util.RandomTodoGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.example.util.RestAssuredHelper.getRequestSpec;

public class GetTodosTest extends BaseTestRestAssured {

    @Test
    @Tag("smoke")
    @DisplayName("GET /todos проверка кода ответа")
    void getStatusCodeTest(){
      given()
              .spec(getRequestSpec(""))
              .when()
              .get()
              .then()
              .statusCode(SC_OK)
              .extract().body().as(new TypeRef<List<Todo>>() {
              }).forEach(System.out::println);
    }

    @Test
    @ExtendWith(TodoParamResolver.class)
    void postTodo(@TodoParam Todo todoRandom){
        System.out.println(todoRandom);
    }
}
