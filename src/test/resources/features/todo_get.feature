Feature: Проверка метода GET /todos с фильтрами

  @cleanDB
  Scenario: [TC-1] Get todos with offset filter
    Given DB should be empty
    And DB should be filled with 5 entities for TC-1
    When send request GET todo with offset 2
    Then responded todo list should contains entities based on offset filter

  @cleanDB
  Scenario: [TC-2] Get todos with limit filter
    Given DB should be empty
    And DB should be filled with 5 entities for TC-2
    When send request GET todo with limit 2
    Then responded todo list should contains entities based on limit filter

  @cleanDB
  Scenario: [TC-3] Get todos with offset and limit filter
    Given DB should be empty
    And DB should be filled with 10 entities for TC-3
    When send request GET todo with offset 3 and limit 2
    Then responded todo list should contains entities based on offset and limit filter

  @cleanDB
  Scenario: [TC-4] Проверка нулевой фильтрации в запросе GET по фильтрам offset
    Given База данных должна быть пустой до тестирования
    And Заполняем базу данных 5 рандомно сгенерированными сущностями для ТС-4
    When Отсылаем GET запрос с фильтром offset равным 0
    Then Должен вернуться аналогичный список сущностей, не изменённый

  @cleanDB
  Scenario: [TC-5] Проверка нулевой фильтрации в запросе GET по фильтрам limit
    Given База данных должна быть пустой до тестирования
    And Заполняем базу данных 5 рандомно сгенерированными сущностями для ТС-5
    When Отсылаем GET запрос с фильтром limit равным 0
    Then Должен вернуться пустой список сущностей