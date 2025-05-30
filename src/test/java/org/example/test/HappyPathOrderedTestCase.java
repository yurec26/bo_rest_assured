package org.example.test;

import io.qameta.allure.*;
import org.example.annotation.TodoParam;
import org.example.model.Todo;
import org.example.util.TodoParamResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
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
        assertTrue(getTodos(SC_OK).isEmpty(),
                "БД должно быть пустым до тестового прогона");

    }

    @Test
    @TmsLink("TA-2")
    @Order(1)
    @Story("Создание задачи POST todos/")
    @DisplayName("Проверка создания новой сущности в БД с валидными данными запроса.")
    @ExtendWith(value = TodoParamResolver.class)
    void postTodoTest(@TodoParam Todo randomTodo) {
        postTodo(randomTodo, SC_CREATED);
        testTodo = randomTodo;
        Todo responsedTodo = getTodos(SC_OK).getFirst();
        assertEquals(testTodo, responsedTodo,
                "В БД должна лежать одна сущность %s".formatted(testTodo));
    }
//
//    @Test
//    @TmsLink("TA-3")
//    @QaseId(3)
//    @Order(2)
//    @Story("Обновление задачи PUT todos/id")
//    @DisplayName("Проверка замены сущности, уже лежащей  в БД, новой с валидными данными запроса.")
//    @ExtendWith(value = TodoParamResolver.class)
//    void putTodoTest(@TodoParam Todo newRandomTodo) {
//        putTodo(testTodo.getId(), newRandomTodo, SC_OK);
//        testTodo = newRandomTodo;
//        Todo responsedTodo = getTodos(SC_OK).getFirst();
//        assertEquals(testTodo, responsedTodo,
//                "В БД должна лежать одна обновленная сущность %s".formatted(testTodo));
//    }
//
//    @Test
//    @TmsLink("TA-4")
//    @QaseId(4)
//    @Order(3)
//    @Story("Удаление задачи DELETE todos/id")
//    @DisplayName("Проверка удаления сущности из БД с валидными данными запроса.")
//    void deleteTodoTest() {
//        deleteTodo(testTodo.getId(), SC_NO_CONTENT);
//        assertTrue(getTodos(SC_OK).isEmpty(),
//                "В списке полученных сущностей не должно находиться сущности, которую мы удаляем.");
//    }
//
//
//    @Test
//    @TmsLink("TA-1")
//    @QaseId(1)
//    @Story("Получение списка задач через GET /todos без фильтров")
//    @DisplayName("Проверка запроса списка всех сущностей из БД без фильтров.")
//    void getAllEntitiesTest() {
//        List<Todo> todosRandom = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            todosRandom.add(generateRandomTodo());
//        }
//        todosRandom.forEach(s -> postTodo(s, SC_CREATED));
//        List<Todo> respondedTodos = getTodos(SC_OK);
//        assertEquals(todosRandom, respondedTodos,
//                "В БД должны лежать именно эти сущности  %s".formatted(todosRandom));
//    }

    //    @Test
    @AfterAll
    @DisplayName("Очищаем БД после прогона")
    void tearDown() {
        deleteAll();
    }
}
