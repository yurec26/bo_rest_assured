package org.example.test;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.example.Todo;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Tag("functional")
@Epic("Управление задачами")
@Feature("GET запросы с фильтрами")
@Severity(SeverityLevel.NORMAL)
@DisplayName("Проверка фильтрации по запросе GET")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetFiltersTest extends BaseTest {

    private List<Todo> TODO_LIST;
    private final Integer TODO_NUMBERS_TO_POST = 5;
    private final Integer OFFSET_FILTER = 2;
    private final Integer LIMIT_FILTER = 2;

    @BeforeAll
    void setUP() throws IOException {
        assertTrue(todoService.getTodos().execute().body().isEmpty(),
                "БД должно быть пустым до тестового прогона");
        TODO_LIST = fillDBWithRandomTodos(TODO_NUMBERS_TO_POST);
    }

    @Test
    @DisplayName("Проверка фильтрации в запросе GET по фильтру offset")
    void testGetWithOffset() throws IOException {
        Response<List<Todo>> response = todoService.getTodosOffset(OFFSET_FILTER).execute();
        assertSoftly(softly -> {
            softly.assertThat(response.body())
                    .as("Фильтр 'offset %s' должен корректно отфильтровывать".formatted(OFFSET_FILTER))
                    .isEqualTo(TODO_LIST.subList(OFFSET_FILTER, TODO_LIST.size()));
            softly.assertThat(response.code())
                    .as("статус ответа должен быть 200")
                    .isEqualTo(SC_OK);
        });
    }

    @Test
    @DisplayName("Проверка фильтрации в запросе GET по фильтру limit")
    void testGetWithLimit() throws IOException {
        Response<List<Todo>> response = todoService.getTodosLimit(LIMIT_FILTER).execute();
        assertSoftly(softly -> {
            softly.assertThat(response.body())
                    .as("Фильтр 'limit %s' должен корректно отфильтровывать".formatted(LIMIT_FILTER))
                    .isEqualTo(TODO_LIST.subList(0, LIMIT_FILTER));
            softly.assertThat(response.code())
                    .as("статус ответа должен быть 200")
                    .isEqualTo(SC_OK);
        });
    }

    @Test
    @DisplayName("Проверка фильтрации в запросе GET по фильтрам offset и limit")
    void testGetWithOffsetAndLimit() throws IOException {
        Response<List<Todo>> response = todoService.getTodosOffsetAndLimit(OFFSET_FILTER, LIMIT_FILTER).execute();
        assertSoftly(softly -> {
            softly.assertThat(response.body())
                    .as("Фильтры 'offset: %s  и limit: %s ' должен корректно отфильтровывать"
                            .formatted(OFFSET_FILTER, LIMIT_FILTER))
                    .isEqualTo(TODO_LIST.subList(OFFSET_FILTER, OFFSET_FILTER + LIMIT_FILTER));
            softly.assertThat(response.code())
                    .as("статус ответа должен быть 200")
                    .isEqualTo(SC_OK);
        });
    }

    @Test
    @DisplayName("Проверка нулевой фильтрации в запросе GET по фильтрам offset")
    void testGetWithZeroOffset() throws IOException {
        Response<List<Todo>> response = todoService.getTodosOffset(0).execute();
        assertSoftly(softly -> {
            softly.assertThat(response.body())
                    .as("Фильтр 'offset %s' должен корректно отфильтровывать".formatted(0))
                    .isEqualTo(TODO_LIST);
            softly.assertThat(response.code())
                    .as("статус ответа должен быть 200")
                    .isEqualTo(SC_OK);
        });
    }

    @Test
    @DisplayName("Проверка нулевой фильтрации в запросе GET по фильтрам limit")
    void testGetWithZeroLimit() throws IOException {
        Response<List<Todo>> response = todoService.getTodosLimit(0).execute();
        assertSoftly(softly -> {
            softly.assertThat(response.body().isEmpty())
                    .as("Фильтр 'limit %s' должен корректно отфильтровывать".formatted(OFFSET_FILTER))
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
