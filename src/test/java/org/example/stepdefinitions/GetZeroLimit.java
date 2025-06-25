package org.example.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.model.Todo;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.example.stepdefinitions.Hooks.fillDBWithEntitiesBase;
import static org.example.util.RestAssuredHelper.getTodosLimit;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetZeroLimit {

    protected final List<Todo> TODO_LIST = new ArrayList<>();
    private List<Todo> TODO_LIST_RECEIVED;

    @Given("Заполняем базу данных {int} рандомно сгенерированными сущностями для ТС-5")
    public void fillDBWithEntities(Integer n) {
        fillDBWithEntitiesBase(n, TODO_LIST);
    }

    @When("Отсылаем GET запрос с фильтром limit равным 0")
    public void getWithOffset() {
        TODO_LIST_RECEIVED = getTodosLimit(0, SC_OK);
    }

    @Then("Должен вернуться пустой список сущностей")
    public void checkStatus() {
        assertTrue(TODO_LIST_RECEIVED.isEmpty(),
                "список должен быть пустой при limit = 0");
    }
}
