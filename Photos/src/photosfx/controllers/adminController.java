package photosfx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import photosfx.models.Admin;
import photosfx.models.User;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class adminController {
    private ObservableList<User> userList;

    public void updateList() {
        userList.clear();
        userList.addAll(Admin.listUsers());
    }

    public void createUser(String username) {
        Admin.createUser(username);
        updateList();
    }

    public void deleteUser(String username) {
        Admin.deleteUser(username);
        updateList();
    }


    
}
