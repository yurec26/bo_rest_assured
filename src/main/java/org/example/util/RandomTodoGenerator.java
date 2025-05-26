package org.example.util;

import org.example.model.Todo;

import java.security.SecureRandom;
import java.util.Random;


public class RandomTodoGenerator {

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz" +
            "0123456789";
    private static final Random RANDOM = new SecureRandom();

    public static Todo generateRandomTodo() {
        return new Todo
                (
                        RANDOM.nextLong(0, Long.MAX_VALUE),
                        randomString(),
                        RANDOM.nextBoolean()
                );
    }

    public static String randomString() {
        int length = RANDOM.nextInt(20) + 1;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length()));
            sb.append(c);
        }
        return sb.toString();
    }
}
