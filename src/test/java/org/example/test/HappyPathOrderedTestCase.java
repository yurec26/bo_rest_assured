package org.example.test;

import io.qameta.allure.*;
import org.example.Todo;
import org.example.TodoParam;
import org.example.TodoParamResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.apache.http.HttpStatus.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
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
public class HappyPathOrderedTestCase extends BaseTest {

    private Todo testTodo;


    @BeforeAll
    @DisplayName("Убеждаемся что БД пуста")
    void setUp() throws IOException {
        assertTrue(Objects.requireNonNull(todoService.getTodos().execute().body()).isEmpty(),
                "БД должно быть пустым до тестового прогона");
    }

    @Test
    @Order(1)
    @Story("Создание задачи POST todos/")
    @DisplayName("Проверка post status code is 201")
    @ExtendWith(value = TodoParamResolver.class)
    void postTodoTest(@TodoParam Todo randomTodo) throws IOException {
        Response<Void> response = todoService.postTodo(randomTodo).execute();
        assertEquals(SC_CREATED, response.code(),
                "статус ответа должен быть 201");
        testTodo = randomTodo;
    }

    @Test
    @Order(2)
    @DisplayName("Проверка get status code is 200")
    @Story("Запрос всех задач GET todos/")
    void getTodoTest() throws IOException {
        Response<List<Todo>> response = todoService.getTodos().execute();
        assertSoftly(softly -> {
            softly.assertThat(response.body().getFirst())
                    .as("В БД должна лежать одна сущность %s".formatted(testTodo))
                    .isEqualTo(testTodo);
            softly.assertThat(response.code())
                    .as("статус ответа должен быть 200")
                    .isEqualTo(SC_OK);
        });
    }

    @Test
    @Order(3)
    @DisplayName("Проверка put status code is 200")
    @Story("Обновление задачи PUT todos/id")
    @ExtendWith(value = TodoParamResolver.class)
    void putTodoTest(@TodoParam Todo newRandomTodo) throws IOException {
        Response<Void> response = todoService
                .putTodo(testTodo.getId(), newRandomTodo).execute();
        assertEquals(SC_OK, response.code(),
                "статус ответа должен быть 200");
        testTodo = newRandomTodo;
    }

    @Test
    @Order(4)
    @DisplayName("Проверка get status code обновленной сущности is 200")
    @Story("Запрос всех задач GET todos/")
    void getUpdatedTodoTest() throws IOException {
        Response<List<Todo>> response = todoService.getTodos().execute();
        assertSoftly(softly -> {
            softly.assertThat(response.body().getFirst())
                    .as("В БД должна лежать одна сущность %s".formatted(testTodo))
                    .isEqualTo(testTodo);
            softly.assertThat(response.code())
                    .as("статус ответа должен быть 200")
                    .isEqualTo(SC_OK);
        });
    }

    @Test
    @Order(5)
    @Story("Удаление задачи DELETE todos/id")
    @DisplayName("Проверка delete status code is 204")
    void deleteTodoTest() throws IOException {
        Response<Void> response = todoService.deleteTodo(testTodo.getId(), auth).execute();
        assertEquals(SC_NO_CONTENT, response.code(),
                "статус ответа должен быть 204");
    }

    @Test
    @Order(6)
    @DisplayName("Проверка get status code is 200, проверка БД - пустое")
    @Story("Запрос всех задач GET todos/")
    void getTodoDeletedTest() throws IOException {
        Response<List<Todo>> response = todoService.getTodos().execute();
        assertSoftly(softly -> {
            softly.assertThat(response.body().isEmpty())
                    .as("БД должна быть пустой")
                    .isTrue();
            softly.assertThat(response.code())
                    .as("статус ответа должен быть 200")
                    .isEqualTo(SC_OK);
        });
    }

    @AfterAll
    @DisplayName("Очищаем БД после прогона")
    void tearDown() throws IOException {
        deleteAllTodos();
    }
}
