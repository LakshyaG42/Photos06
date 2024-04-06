package photosfx;

import java.io.File;
import java.util.Set;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import photosfx.models.Admin;
import photosfx.models.Album;
import photosfx.models.Tags;
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
            boolean stockExists = false;
            for(String username : Admin.getUsernameList()) {
                if(username.equals("stock")) {
                    stockExists = true;
                }
            }
            if(!stockExists){
                Admin.createUser("stock");
            }
        }
        for(User user : Admin.getUsers()) {
            if(user.getUsername().equals("stock")) {
                if(user.getAlbum("stock") == null) {
                    user.addAlbum(new photosfx.models.Album("stock"));
                    Album stockalbum = user.getAlbum("stock");
                    Tags nj = new Tags();
                    nj.addTag("Location", "New Jersey");
                    nj.addTag("Mood", "Calm");
                    Tags happy = new Tags();
                    happy.addTag("Location", "Park");
                    happy.addTag("Mood", "Happy");
                    happy.addTag("Person", "Doggo");
                    Tags sad = new Tags();
                    sad.addTag("Location", "English Class");
                    sad.addTag("Mood", "Sad");
                    sad.addTag("Person", "Catto");
                    Tags stand = new Tags();
                    stand.addTag("Location", "UN Conference Hall");
                    stand.addTag("Mood", "Confident");
                    stand.addTag("Person", "Catto");
                    Tags meth = new Tags();
                    meth.addTag("Location", "Software Methodology Classroom");
                    meth.addTag("Mood", "Estatic");
                    meth.addTag("Person", Set.of("Hackerman", "Estatic"));
                    stockalbum.addPhoto(new photosfx.models.Photo("Photos/data/stockPhotos/definitelyNewJersey.jpg", photosfx.models.Photo.getLastModifiedDateTime(new File("Photos/data/stockPhotos/definitelyNewJersey.jpg")), "New Jersey", nj));
                    stockalbum.addPhoto(new photosfx.models.Photo("Photos/data/stockPhotos/happydog.jpg", photosfx.models.Photo.getLastModifiedDateTime(new File("Photos/data/stockPhotos/happydog.jpg")), "Happy Dog", happy));
                    stockalbum.addPhoto(new photosfx.models.Photo("Photos/data/stockPhotos/sadcat.jpg", photosfx.models.Photo.getLastModifiedDateTime(new File("Photos/data/stockPhotos/sadcat.jpg")), "Sad Cat", sad));
                    stockalbum.addPhoto(new photosfx.models.Photo("Photos/data/stockPhotos/standingcat.jpg", photosfx.models.Photo.getLastModifiedDateTime(new File("Photos/data/stockPhotos/standingcat.jpg")), "Standing Cat", stand));
                    stockalbum.addPhoto(new photosfx.models.Photo("Photos/data/stockPhotos/thisIsUs.png", photosfx.models.Photo.getLastModifiedDateTime(new File("Photos/data/stockPhotos/thisIsUs.png")), "This Is Us", meth));
                    Admin.saveUsers("Photos/data/users.ser");
                }
            }
        }
        launch(args);
    }
    
}