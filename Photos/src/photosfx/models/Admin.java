package photosfx.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Admin Model
 * @author Dhruv Shidhaye
 * @author Lakshya Gour
 */

public class Admin {
    private static List<User> users;
    static {
        users = new ArrayList<>();
        String filePath = "Photos/data/users.ser";
        loadUsers(filePath);
        // User stockUser = new User("stock");
        // if(users.isEmpty()) {
        //     users.add(stockUser);
        // }
    }
    
    /**
 * Returns a list of all users.
 * 
 * @return a list containing all registered users
 */
    public static List<User> getUsers() {
        return users;
    }

    /**
 * Prints and returns the list of usernames.
 * 
 * @return a list containing the usernames of all registered users
 */
    public static List<User> listUsers() {
        return users;
    }

    /**
 * Retrieves a list of all usernames.
 * 
 * @return an ArrayList containing the usernames of all registered users
 */
    public static ArrayList<String> getUsernameList() {
        ArrayList<String> usernames = new ArrayList<>();
        for(User user : users) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }

    /**
 * Creates a new user with the specified username.
 * 
 * @param username the username for the new user
 * @throws IllegalArgumentException if the username is null, empty, or already exists
 */
    public static void createUser(String username) {
        if(username == null || username.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid username.");
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        for(User user : users) {
            if(user.getUsername().equals(username) || username.equals("admin")) {
                //showAlert(Alert.AlertType.ERROR, "Error", "Username already exists");
                throw new IllegalArgumentException("Username already exists");
            }
        }
        User newUser = new User(username);
        users.add(newUser);
        //create ser file for users' photos here
        // File userFile = new File("Photos/data/userPhotos/" + username + ".ser");
        // try {
        //     if(userFile.createNewFile()) {
        //         
        //     } else {
        //         
        //     }
        // } catch (IOException e) {
        //     showAlert(Alert.AlertType.ERROR, "Error creating user's data:", e.getMessage());
        //     
        // }
        
    }

    /**
 * Deletes the user with the specified username.
 * 
 * @param username the username of the user to delete
 * @throws IllegalArgumentException if the username is null, empty, or the user does not exist
 */

    public static void deleteUser(String username) {
        if(username == null || username.isEmpty()) {
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
        // try {
        //     if(!userToDelete.getUsername().equals("stock")) {
        //         File file = new File("Photos/data/userPhotos/" + userToDelete.getUsername() + ".ser");
        //         if(file.delete()) {
        //             
        //         } else {
        //             
        //         }
        //     }
        // } catch (Exception e) {
        //     showAlert(Alert.AlertType.ERROR, "Error deleting user's data:", e.getMessage());
        //     System.err.println("Error deleting user's data: " + e.getMessage());
        // }
    }


    /**
 * Saves the list of users to the specified file.
 * 
 * @param fileName the file path where users data will be saved
 */

    public static void saveUsers(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(users);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error saving users:", e.getMessage());
        }
    }

    /**
 * Loads the list of users from the specified file.
 * 
 * @param fileName the file path from where users data will be loaded
 */
    public static void loadUsers(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            users = (List<User>) inputStream.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading users:", e.getMessage());
            
        }
    }

    /**
 * Displays an alert dialog with the specified type, title, and message.
 * 
 * @param alertType the type of the alert dialog
 * @param title the title of the alert dialog
 * @param message the message to display in the alert dialog
 */
    public static Optional<ButtonType> showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(
            new Image("file:///" + new File("Photos/data/userPhotos/LogoMain.png").getAbsolutePath().replace("\\", "/")));
        return alert.showAndWait();
    }

    @Override
    public String toString() {
        return "Admin{" +
                "users=" + users +
                '}';
    }
}
