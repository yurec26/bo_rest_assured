package org.example.util;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.model.Todo;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;

public class RestAssuredHelper {

    private final static String URL = "http://5.129.198.140:8080";
    private final static String TODO_PATH = "/todos/";
    private final static String LOGIN = "admin";
    private final static String PASS = "admin";
    private final static String OFFSET_PARAM = "offset";
    private final static String LIMIT_PARAM = "limit";

    public static RequestSpecBuilder getRequestSpec(String id) {
        return new RequestSpecBuilder()
                .setBaseUri(URL)
                .setBasePath(TODO_PATH + id);
    }

    @Step("GET/todos без фильтров")
    public static List<Todo> getTodos(int ExpectedCode) {
        return getBase(getRequestSpec("").build()
                , ExpectedCode);
    }

    @Step("GET/todos с фильтром 'offset={offset}' ")
    public static List<Todo> getTodosOffset(Integer offset, int ExpectedCode) {
        return getBase(getRequestSpec("")
                        .addQueryParam(OFFSET_PARAM, offset)
                        .build()
                , ExpectedCode);
    }

    @Step("GET/todos с фильтром 'limit'={limit}' ")
    public static List<Todo> getTodosLimit(Integer limit, int ExpectedCode) {
        return getBase(getRequestSpec("")
                        .addQueryParam(LIMIT_PARAM, limit)
                        .build(),
                ExpectedCode);
    }

    @Step("GET/todos с фильтрами 'offset={offset}' / 'limit'={limit}' ")
    public static List<Todo> getTodosOffsetAndLimit(Integer offset,
                                                    Integer limit,
                                                    int ExpectedCode) {
        return getBase(getRequestSpec("")
                        .addQueryParams(Map.of(OFFSET_PARAM, offset, LIMIT_PARAM, limit))
                        .build(),
                ExpectedCode);
    }

    public static List<Todo> getBase(RequestSpecification requestSpecification,
                                     int ExpectedCode) {
        return given()
                .filter(new AllureRestAssured())
                .spec(requestSpecification)
                .when()
                .get()
                .then()
                .statusCode(ExpectedCode)
                .extract().body().as(new TypeRef<>() {
                });
    }

    @Step("POST/todos : {todo}")
    public static void postTodo(Todo todo, int ExpectedCode) {
        given()
                .filter(new AllureRestAssured())
                .spec(getRequestSpec("").build())
                .contentType(ContentType.JSON)
                .body(todo)
                .when()
                .post()
                .then()
                .statusCode(ExpectedCode);
    }

    @Step("DELETE/todos : {id}")
    public static void deleteTodo(Long id,
                                  int ExpectedCode) {
        given()
                .filter(new AllureRestAssured())
                .spec(getRequestSpec(String.valueOf(id)).build())
                .auth().preemptive().basic(LOGIN, PASS)
                .when()
                .delete()
                .then()
                .statusCode(ExpectedCode);
    }

    @Step("PUT/todos with id: {id}, replacement: {todo}")
    public static void putTodo(Long id, Todo todo,
                               int ExpectedCode) {
        given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .spec(getRequestSpec(String.valueOf(id)).build())
                .body(todo)
                .when()
                .put()
                .then()
                .statusCode(ExpectedCode);
    }

    public static void deleteAll() {
        getTodos(SC_OK)
                .forEach(s -> deleteTodo(s.getId(), SC_NO_CONTENT));
    }
}
