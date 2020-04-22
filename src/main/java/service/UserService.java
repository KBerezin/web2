package service;

import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public final class UserService {

    private static UserService INSTANCE;

    private Map<Long, User> dataBase = Collections.synchronizedMap(new HashMap<>());
    private AtomicLong maxId = new AtomicLong(1000);
    private Map<Long, User> authMap = Collections.synchronizedMap(new HashMap<>());

    private UserService() {
    }

    public synchronized static UserService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserService();
        }
        return INSTANCE;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(dataBase.values());
    }

    public User getUserById(Long id) {
        return dataBase.get(id);
    }

    public boolean addUser(User user) {
        if (!isExistsThisUser(user)) {
            user.setId(maxId.incrementAndGet());
            dataBase.put(user.getId(), user);
            return true;
        }
        return false;
    }

    public void deleteAllUser() {
        dataBase = Collections.synchronizedMap(new HashMap<>());
    }

    public boolean isExistsThisUser(User user) {
        return dataBase.containsValue(user);
    }

    public List<User> getAllAuth() {
        return new ArrayList<>(authMap.values());
    }


    public boolean authUser(User user) {
        User existingUser = getAllUsers().stream().filter(user::equals).findFirst().orElse(null);

        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            authMap.put(existingUser.getId(), existingUser);
            return true;
        }

        return false;
    }

    public void logoutAllUsers() {
        authMap = Collections.synchronizedMap(new HashMap<>());
    }

    public boolean isUserAuthById(Long id) {
        return authMap.containsKey(id);
    }

}
