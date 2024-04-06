package photosfx.models;

import java.util.List;

public class Admin {
    private static List<User> users;

    public static List<User> listUsers() {
        for(User user : users) {
            System.out.println(user.getUsername());
        }
        return users;
    }

    public static void createUser(String username) {
        if(username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        for(User user : users) {
            if(user.getUsername().equals(username)) {
                throw new IllegalArgumentException("Username already exists");
            }
        }
        User newUser = new User(username);
        users.add(newUser);
    }

    public static void deleteUser(String username) {
        if(username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        User userToDelete = null;
        for(User user : users) {
            if(user.getUsername().equals(username)) {
                userToDelete = user;
                break;
            }
        }
        if(userToDelete == null) {
            throw new IllegalArgumentException("User does not exist");
        }
        users.remove(userToDelete);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "users=" + users +
                '}';
    }
}
