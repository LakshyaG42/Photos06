package photosfx;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import photosfx.models.Admin;
import photosfx.models.Album;
import photosfx.models.User;
import photosfx.controllers.loginController;

/**
 * Main
 * @author Dhruv Shidhaye
 * @author Lakshya Gour
 */


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/photosfx/view/login.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 400, 300));
        loginController controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.show();
    }


    public static void main(String[] args) {

        Admin.loadUsers("Photos/data/users.ser");
        if(Admin.getUsers().isEmpty()) {
            Admin.createUser("stock");
        }
        for(User user : Admin.getUsers()) {
            if(user.getUsername().equals("stock")) {
                if(user.getAlbum("stock") == null) {
                    user.addAlbum(new photosfx.models.Album("stock"));
                    Album stockalbum = user.getAlbum("stock");
                    stockalbum.addPhoto(new photosfx.models.Photo("Photos/data/stockPhotos/definitelyNewJersey.jpg", photosfx.models.Photo.getLastModifiedDateTime(new File("Photos/data/stockPhotos/definitelyNewJersey.jpg")), "New Jersey"));
                    stockalbum.addPhoto(new photosfx.models.Photo("Photos/data/stockPhotos/happydog.jpg", photosfx.models.Photo.getLastModifiedDateTime(new File("Photos/data/stockPhotos/happydog.jpg")), "Happy Dog"));
                    stockalbum.addPhoto(new photosfx.models.Photo("Photos/data/stockPhotos/sadcat.jpg", photosfx.models.Photo.getLastModifiedDateTime(new File("Photos/data/stockPhotos/sadcat.jpg")), "Sad Cat"));
                    stockalbum.addPhoto(new photosfx.models.Photo("Photos/data/stockPhotos/standingcat.jpg", photosfx.models.Photo.getLastModifiedDateTime(new File("Photos/data/stockPhotos/standingcat.jpg")), "Standing Cat"));
                    stockalbum.addPhoto(new photosfx.models.Photo("Photos/data/stockPhotos/thisIsUs.png", photosfx.models.Photo.getLastModifiedDateTime(new File("Photos/data/stockPhotos/thisIsUs.png")), "This Is Us"));
                    Admin.saveUsers("Photos/data/users.ser");
                }
            }
        }
        launch(args);
    }
}