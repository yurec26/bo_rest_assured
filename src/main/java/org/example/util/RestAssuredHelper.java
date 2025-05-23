package org.example.util;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RestAssuredHelper {

    public static RequestSpecification getRequestSpec(
            String id
    ) {

        return new RequestSpecBuilder()
                .setBaseUri("http://5.129.198.140:8080")
                .setBasePath("todos/%s".formatted(id))
                .build();
    }

}
