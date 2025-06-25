package org.example.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.model.Todo;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.example.stepdefinitions.Hooks.fillDBWithEntitiesBase;
import static org.example.util.RestAssuredHelper.getTodosOffsetAndLimit;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetStepsWithOffsetAndLimit {

    protected final List<Todo> TODO_LIST = new ArrayList<>();
    private List<Todo> TODO_LIST_RECEIVED;
    private int offsetFilter = 0;
    private int limitFilter = 0;

    @Given("DB should be filled with {int} entities for TC-3")
    public void fillDBWithEntities(Integer n) {
        fillDBWithEntitiesBase(n, TODO_LIST);
    }

    @When("send request GET todo with offset {int} and limit {int}")
    public void getWithLimit(Integer offset, Integer limit) {
        offsetFilter = offset;
        limitFilter = limit;
        TODO_LIST_RECEIVED = getTodosOffsetAndLimit(offset, limit, SC_OK);
    }

    @Then("responded todo list should contains entities based on offset and limit filter")
    public void checkStatus() {
        assertEquals(TODO_LIST.subList(offsetFilter, offsetFilter + limitFilter), TODO_LIST_RECEIVED,
                "Фильтры 'offset: %s  и limit: %s ' должен корректно отфильтровывать"
                        .formatted(offsetFilter, limitFilter));
    }
}
