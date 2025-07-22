package org.example.test;

import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.example.util.RandomTodoGenerator.generateRandomTodo;
import static org.example.util.RestAssuredHelper.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Epic("Управление задачами")
@Severity(SeverityLevel.NORMAL)
@DisplayName("Проверка схемы JSON ответа при запросе GET todos/")
public class JsonResponseSchemaValidationTest {

    private static final Integer TODO_NUMBERS_TO_POST = 5;

    @BeforeAll
    static void setUP() {
        assertTrue(getTodos(SC_OK).isEmpty(),
                "БД должно быть пустым до тестового прогона");
        for (int i = 0; i < TODO_NUMBERS_TO_POST; i++) {
            postTodo(generateRandomTodo(), SC_CREATED);
        }
    }

    @Test
    void testJsonSchema() {
        given()
                .filter(new AllureRestAssured())
                .spec(getRequestSpec("").build())
                .when()
                .get()
                .then()
                .body(matchesJsonSchemaInClasspath("todos_schema.json"))
                .statusCode(SC_OK);
    }

    @AfterAll
    @Step("Очищаем БД после прогона")
    static void tearDown() {
        deleteAll();
    }
}
