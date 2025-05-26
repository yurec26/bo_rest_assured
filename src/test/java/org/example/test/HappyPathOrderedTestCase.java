package org.example.test;

import io.qameta.allure.*;
import org.example.annotation.TodoParam;
import org.example.model.Todo;
import org.example.util.TodoParamResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.apache.http.HttpStatus.*;
import static org.example.util.RestAssuredHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("smoke")
@Epic("Управление задачами")
@Feature("CRUD")
@DisplayName("Прогон критического пути пользователя со рандомными сущностями")
@Execution(ExecutionMode.SAME_THREAD)
@Severity(SeverityLevel.CRITICAL)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HappyPathOrderedTestCase {

    private Todo testTodo;

    @BeforeAll
    @DisplayName("Убеждаемся что БД пуста")
    void setUp() {
        // можно assumeTrue
        assertTrue(getTodos(SC_OK).isEmpty(),
                "БД должно быть пустым до тестового прогона");
    }

    @Test
    @Order(1)
    @Story("Создание задачи POST todos/")
    @DisplayName("Проверка post status code is 201")
    @ExtendWith(value = TodoParamResolver.class)
    void postTodoTest(@TodoParam Todo randomTodo) {
        postTodo(randomTodo, SC_CREATED);
        testTodo = randomTodo;
    }

    @Test
    @Order(2)
    @DisplayName("Проверка get status code is 200")
    @Story("Запрос всех задач GET todos/")
    void getTodoTest() {
        Todo responsedTodo = getTodos(SC_OK).getFirst();
        assertEquals(testTodo, responsedTodo,
                "В БД должна лежать одна сущность %s".formatted(testTodo));
    }

    @Test
    @Order(3)
    @DisplayName("Проверка put status code is 200")
    @Story("Обновление задачи PUT todos/id")
    @ExtendWith(value = TodoParamResolver.class)
    void putTodoTest(@TodoParam Todo newRandomTodo) {
        putTodo(testTodo.getId(), newRandomTodo, SC_OK);
        testTodo = newRandomTodo;
    }

    @Test
    @Order(4)
    @DisplayName("Проверка get status code обновленной сущности is 200")
    @Story("Запрос всех задач GET todos/")
    void getUpdatedTodoTest() {
        Todo responsedTodo = getTodos(SC_OK).getFirst();
        assertEquals(testTodo, responsedTodo,
                "В БД должна лежать одна обновленная сущность %s".formatted(testTodo));
    }

    @Test
    @Order(5)
    @Story("Удаление задачи DELETE todos/id")
    @DisplayName("Проверка delete status code is 204")
    void deleteTodoTest() {
        deleteTodo(testTodo.getId(), SC_NO_CONTENT);
    }

    @Test
    @Order(6)
    @DisplayName("Проверка get status code is 200, проверка БД - пустое")
    @Story("Запрос всех задач GET todos/")
    void getTodoDeletedTest() {
        assertTrue(getTodos(SC_OK).isEmpty(),
                "БД должна быть пустой");
    }

    //    @Test
    @AfterAll
    @DisplayName("Очищаем БД после прогона")
    void tearDown() {
        deleteAll();
    }
}
