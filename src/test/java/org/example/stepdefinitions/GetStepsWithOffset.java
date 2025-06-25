package org.example.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.model.Todo;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.example.util.RestAssuredHelper.getTodos;
import static org.example.util.RestAssuredHelper.getTodosOffset;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.example.stepdefinitions.Hooks.fillDBWithEntitiesBase;

public class GetStepsWithOffset {

    protected final List<Todo> TODO_LIST = new ArrayList<>();
    private List<Todo> TODO_LIST_RECEIVED;
    private int offsetFilter = 0;

    @Given("DB should be empty")
    public void assertThatDBisEmpty() {
        assertTrue(getTodos(SC_OK).isEmpty(),
                "БД должно быть пустым до тестового прогона");
    }

    @Given("База данных должна быть пустой до тестирования")
    public void assertThatDBisEmpty_RUS() {
        assertTrue(getTodos(SC_OK).isEmpty(),
                "БД должно быть пустым до тестового прогона");
    }

    @Given("DB should be filled with {int} entities for TC-1")
    public void fillDBWithEntities(Integer n) {
        fillDBWithEntitiesBase(n, TODO_LIST);
    }

    @When("send request GET todo with offset {int}")
    public void getWithOffset(Integer offset) {
        offsetFilter = offset;
        TODO_LIST_RECEIVED = getTodosOffset(offset, SC_OK);
    }

    @Then("responded todo list should contains entities based on offset filter")
    public void checkStatus() {
        assertEquals(TODO_LIST.subList(offsetFilter, TODO_LIST.size()), TODO_LIST_RECEIVED,
                "Фильтр 'offset %s' должен корректно отфильтровывать".formatted(offsetFilter));
    }
}
