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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetStepsWithLimit {

    protected final List<Todo> TODO_LIST = new ArrayList<>();
    private List<Todo> TODO_LIST_RECEIVED;
    private int limitFilter = 0;

    @Given("DB should be filled with {int} entities for TC-2")
    public void fillDBWithEntities(Integer n) {
        fillDBWithEntitiesBase(n, TODO_LIST);
    }

    @When("send request GET todo with limit {int}")
    public void getWithLimit(Integer limit) {
        limitFilter = limit;
        TODO_LIST_RECEIVED = getTodosLimit(limit, SC_OK);
    }

    @Then("responded todo list should contains entities based on limit filter")
    public void checkStatus() {
        assertEquals(TODO_LIST.subList(0, limitFilter), TODO_LIST_RECEIVED,
                "Фильтр 'limit %s' должен корректно отфильтровывать".formatted(limitFilter));
    }
}
