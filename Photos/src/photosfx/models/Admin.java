package photosfx.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;

public class Admin {
    private static List<User> users;
    static {
        users = new ArrayList<>();
        String filePath = "Photos/data/users.ser";
        loadUsers(filePath);
        User stockUser = new User("stock");
        if(users.isEmpty()) {
            users.add(stockUser);
        }
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
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid username.");
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        for(User user : users) {
            if(user.getUsername().equals(username) || username.equals("admin")) {
                showAlert(Alert.AlertType.ERROR, "Error", "Username already exists");
                throw new IllegalArgumentException("Username already exists");
            }
        }
        User newUser = new User(username);
        users.add(newUser);
        //create ser file for users' photos here
        File userFile = new File("Photos/data/userPhotos/" + username + ".ser");
        try {
            if(userFile.createNewFile()) {
                System.out.println("File created: " + userFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error creating user's data:", e.getMessage());
            System.err.println("Error creating user's data: " + e.getMessage());
        }
        
    }

    public static void deleteUser(String username) {
        if(username == null || username.isEmpty() || username.equals("stock")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Username cannot be null or empty or stock");
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
            showAlert(Alert.AlertType.ERROR, "Error", "User does not exist");
            throw new IllegalArgumentException("User does not exist");
        }
        users.remove(userToDelete);

        //delete username.ser file for users here
        try {
            if(!userToDelete.getUsername().equals("stock")) {
                File file = new File("Photos/data/userPhotos/" + userToDelete.getUsername() + ".ser");
                if(file.delete()) {
                    System.out.println(file.getName() + " is deleted!");
                } else {
                    System.out.println("Delete operation is failed.");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error deleting user's data:", e.getMessage());
            System.err.println("Error deleting user's data: " + e.getMessage());
        }
    }


    public static void saveUsers(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(users);
            System.out.println("Users saved successfully.");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error saving users:", e.getMessage());
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
    public static void loadUsers(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            users = (List<User>) inputStream.readObject();
            System.out.println("Users loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading users:", e.getMessage());
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public String toString() {
        return "Admin{" +
                "users=" + users +
                '}';
    }
}
