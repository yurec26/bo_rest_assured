package org.example.util;

import org.example.annotation.TodoParam;
import org.example.model.Todo;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import static org.example.util.RandomTodoGenerator.generateRandomTodo;

public class TodoParamResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext)
            throws ParameterResolutionException {

        return parameterContext.isAnnotated(TodoParam.class)
                && parameterContext.getParameter().getType().equals(Todo.class);
    }

    @Override
    public Todo resolveParameter(ParameterContext parameterContext,
                                   ExtensionContext extensionContext)
            throws ParameterResolutionException {

        return generateRandomTodo();
    }
}
