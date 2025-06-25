package org.example.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.model.Todo;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.example.stepdefinitions.Hooks.fillDBWithEntitiesBase;
import static org.example.util.RestAssuredHelper.getTodosOffset;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetZeroOffset {

    protected final List<Todo> TODO_LIST = new ArrayList<>();
    private List<Todo> TODO_LIST_RECEIVED;

    @Given("Заполняем базу данных {int} рандомно сгенерированными сущностями для ТС-4")
    public void fillDBWithEntities(Integer n) {
        fillDBWithEntitiesBase(n, TODO_LIST);
    }

    @When("Отсылаем GET запрос с фильтром offset равным 0")
    public void getWithOffset() {
        TODO_LIST_RECEIVED = getTodosOffset(0, SC_OK);
    }

    @Then("Должен вернуться аналогичный список сущностей, не изменённый")
    public void checkStatus() {
        assertEquals(TODO_LIST, TODO_LIST_RECEIVED,
                "Фильтры 'offset: %s' должен корректно отфильтровывать"
                        .formatted(0));
    }
}
