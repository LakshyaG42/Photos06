package photosfx.models;

import java.util.ArrayList;
import java.util.List;

public class Admin {
    private static List<User> users;
    static {
        users = new ArrayList<>();
        User stockUser = new User("stock");
        users.add(stockUser);
    }

    public static List<User> listUsers() {
        for(User user : users) {
            System.out.println(user.getUsername());
        }
        return users;
    }
    public static ArrayList<String> getUsernameList() {
        ArrayList<String> usernames = new ArrayList<>();
        for(User user : users) {
            usernames.add(user.getUsername());
        }
        return usernames;
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
        if(username == null || username.isEmpty() || username.equals("stock")) {
            throw new IllegalArgumentException("Username cannot be null or empty or stock");
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
