package com.btcturk.apitest.util;

import com.btcturk.apitest.infrastructure.user.User;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ReaderUtils {
    public static final String JSON_PATH = "src/test/resources/data";

    public static Map<Integer, User> readUsers() {
        log.info("Reading users.");
        // dummy, get users for now
        Map<Integer, User> users = new HashMap<>();
        for (int i = 0; i < 20; i++) {
            User user = new User(
                    i + 1,
                    "user" + i,
                    "password",
                    "token");
            users.put(user.getId(), user);
        }
        return users;
    }

    public static File getJsonFile(String... path) {
        StringBuilder builder = new StringBuilder();
        builder.append(JSON_PATH);
        Arrays.stream(path).iterator().forEachRemaining(o -> {
            builder.append('/');
            builder.append(o);
        });
        String builtPath = builder.toString();
        log.info("Getting JSON -> path: {}", builtPath);
        return new File(builtPath);
    }
}
