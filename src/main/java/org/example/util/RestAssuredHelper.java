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

    private final static String URL = "http://localhost:8080";
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
    public static List<Todo> getTodos(int expectedCode) {
        return getBase(getRequestSpec("").build()
                , expectedCode);
    }

    @Step("GET/todos с фильтром 'offset={offset}' ")
    public static List<Todo> getTodosOffset(int offset,
                                            int expectedCode) {
        return getBase(getRequestSpec("")
                        .addQueryParam(OFFSET_PARAM, offset)
                        .build()
                , expectedCode);
    }

    @Step("GET/todos с фильтром 'limit'={limit}' ")
    public static List<Todo> getTodosLimit(int limit,
                                           int expectedCode) {
        return getBase(getRequestSpec("")
                        .addQueryParam(LIMIT_PARAM, limit)
                        .build(),
                expectedCode);
    }

    @Step("GET/todos с фильтрами 'offset={offset}' / 'limit'={limit}' ")
    public static List<Todo> getTodosOffsetAndLimit(int offset,
                                                    int limit,
                                                    int expectedCode) {
        return getBase(getRequestSpec("")
                        .addQueryParams(Map.of(OFFSET_PARAM, offset, LIMIT_PARAM, limit))
                        .build(),
                expectedCode);
    }

    public static List<Todo> getBase(RequestSpecification requestSpecification,
                                     int expectedCode) {
        return given()
                .filter(new AllureRestAssured())
                .spec(requestSpecification)
                .when()
                .get()
                .then()
                .statusCode(expectedCode)
                .extract().body().as(new TypeRef<>() {
                });
    }

    @Step("POST/todos : {todo}")
    public static void postTodo(Todo todo,
                                int expectedCode) {
        given()
                .filter(new AllureRestAssured())
                .spec(getRequestSpec("").build())
                .contentType(ContentType.JSON)
                .body(todo)
                .when()
                .post()
                .then()
                .statusCode(expectedCode);
    }

    @Step("DELETE/todos : {id}")
    public static void deleteTodo(long id,
                                  int expectedCode) {
        given()
                .filter(new AllureRestAssured())
                .spec(getRequestSpec(String.valueOf(id)).build())
                .auth().preemptive().basic(LOGIN, PASS)
                .when()
                .delete()
                .then()
                .statusCode(expectedCode);
    }

    @Step("PUT/todos with id: {id}, replacement: {todo}")
    public static void putTodo(long id,
                               Todo todo,
                               int expectedCode) {
        given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .spec(getRequestSpec(String.valueOf(id)).build())
                .body(todo)
                .when()
                .put()
                .then()
                .statusCode(expectedCode);
    }

    public static void deleteAll() {
        getTodos(SC_OK)
                .forEach(s -> deleteTodo(s.getId(), SC_NO_CONTENT));
    }
}
