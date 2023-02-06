package com.btcturk.apitest.infrastructure.user;

import com.btcturk.apitest.util.ReaderUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class UserPool {
    private final Map<Integer, User> in;
    private final Map<Integer, User> out = new HashMap<>();
    private final ThreadLocal<UserRetriever> retriever;
    private final ThreadLocal<UserReceder> receder;
    private final ThreadLocal<List<User>> busy;
    private static UserPool userPool;

    private UserPool() {
        this.in = ReaderUtils.readUsers();
        // this guys specific for every concurrent tests.
        this.retriever = ThreadLocal.withInitial(UserRetriever::new);
        this.receder = ThreadLocal.withInitial(UserReceder::new);
        this.busy = ThreadLocal.withInitial(ArrayList::new);
    }

    // singleton; we have only one user pool for all tests.
    public static UserPool getInstance() {
        if (userPool == null) {
            userPool = new UserPool();
        }
        return userPool;
    }

    public UserRetriever retriever() {
        return this.retriever.get();
    }

    public UserReceder receder() {
        return this.receder.get();
    }

    // pulling user from the user pool.
    private User pull(User user) {
        log.info("Pulling user with -> id: {}, username: {}, password: {}",
                user.getId(), user.getUsername(), user.getPassword());
        in.remove(user.getId());
        user.setIsBusy(true);
        out.put(user.getId(), user);
        return user;
    }

    // pushing back user to the user pool.
    private void push(User user) {
        log.info("Pushing back user with -> id: {}, username: {}, password: {}",
                user.getId(), user.getUsername(), user.getPassword());
        out.remove(user.getId());
        user.setIsBusy(false);
        in.put(user.getId(), user);
    }

    public class UserRetriever {

        // retrieves random user from the pool
        public synchronized User one() {
            availabilityCheck(1);
            User user = pull(in.values().stream().toList().get(new Random().nextInt(in.size())));
            busy.get().add(user);
            return user;
        }

        // retrieves random users from the pool.
        public synchronized List<User> many(Integer userCount) {
            availabilityCheck(userCount);
            List<User> users = new ArrayList<>();
            for (int i = 0; i < userCount; i++) {
                User user = pull(in.values().stream().toList().get(new Random().nextInt(in.size())));
                busy.get().add(user);
                users.add(user);
            }
            return users;
        }

        // checking user pool for demanding users.
        @SneakyThrows
        private void availabilityCheck(int userCount) {
            if (userCount > in.size() + out.size()) {
                log.error("User demand exceeding total users size. Exiting..");
                throw new RuntimeException();
            }
            int tryCounter = 1;
            while (in.size() < userCount && tryCounter <= 3) {
                log.warn("There no available user(s) right now! Waiting..");
                wait(2500);
                //
                if (in.size() >= userCount) {
                    log.info("Found available user(s). Continuing..");
                    break;
                } else if (tryCounter == 3) {
                    log.error("Couldn't found any available user(s). Exiting..");
                    throw new RuntimeException();
                }
                tryCounter++;
            }
        }
    }

    public class UserReceder {

        // we have to recede users back to the user pool when test finished.
        public void recede() {
            for (User user : busy.get()) {
                push(user);
            }
        }

    }

}
