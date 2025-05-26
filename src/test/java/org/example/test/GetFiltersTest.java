package org.example.test;

import io.qameta.allure.*;
import org.example.model.Todo;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.example.util.RandomTodoGenerator.generateRandomTodo;
import static org.example.util.RestAssuredHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("functional")
@Epic("Управление задачами")
@Feature("GET запросы с фильтрами")
@Severity(SeverityLevel.NORMAL)
@DisplayName("Проверка фильтрации по запросе GET")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetFiltersTest {

    private final List<Todo> TODO_LIST = new ArrayList<>();
    private final Integer TODO_NUMBERS_TO_POST = 5;
    private final Integer OFFSET_FILTER = 2;
    private final Integer LIMIT_FILTER = 2;


    @BeforeAll
    void setUP() {
        assertTrue(getTodos(SC_OK).isEmpty(),
                "БД должно быть пустым до тестового прогона");
        for (int i = 0; i < TODO_NUMBERS_TO_POST; i++) {
            TODO_LIST.add(generateRandomTodo());
        }
        TODO_LIST.forEach(s -> postTodo(s, SC_CREATED));
    }

    @Test
    @DisplayName("Проверка фильтрации в запросе GET по фильтру offset")
    void testGetWithOffset() {
        List<Todo> todosWithOffset = getTodosOffset(OFFSET_FILTER, SC_OK);
        assertEquals(TODO_LIST.subList(OFFSET_FILTER, TODO_LIST.size()), todosWithOffset,
                "Фильтр 'offset %s' должен корректно отфильтровывать".formatted(OFFSET_FILTER));
    }

    @Test
    @DisplayName("Проверка фильтрации в запросе GET по фильтру limit")
    void testGetWithLimit() {
        List<Todo> todosWithLimit = getTodosLimit(LIMIT_FILTER, SC_OK);
        assertEquals(TODO_LIST.subList(0, LIMIT_FILTER), todosWithLimit,
                "Фильтр 'limit %s' должен корректно отфильтровывать".formatted(LIMIT_FILTER));
    }

    @Test
    @DisplayName("Проверка фильтрации в запросе GET по фильтрам offset и limit")
    void testGetWithOffsetAndLimit() {
        List<Todo> todosWithLimit = getTodosOffsetAndLimit(OFFSET_FILTER, LIMIT_FILTER, SC_OK);
        assertEquals(TODO_LIST.subList(OFFSET_FILTER, OFFSET_FILTER + LIMIT_FILTER), todosWithLimit,
                "Фильтры 'offset: %s  и limit: %s ' должен корректно отфильтровывать"
                        .formatted(OFFSET_FILTER, LIMIT_FILTER));
    }

    @Test
    @DisplayName("Проверка нулевой фильтрации в запросе GET по фильтрам offset")
    void testGetWithZeroOffset() {
        List<Todo> todosWithOffset = getTodosOffset(0, SC_OK);
        assertEquals(TODO_LIST, todosWithOffset,
                "Фильтры 'offset: %s' должен корректно отфильтровывать"
                        .formatted(0));
    }

    @Test
    @DisplayName("Проверка нулевой фильтрации в запросе GET по фильтрам limit")
    void testGetWithZeroLimit() {
        List<Todo> todosWithLimit = getTodosLimit(0, SC_OK);
        assertTrue(todosWithLimit.isEmpty(),
                "список должен быть пустой при limit = 0");
    }

    @AfterAll
    @Step("Очищаем БД после прогона")
    void tearDown() {
        deleteAll();
    }
}
