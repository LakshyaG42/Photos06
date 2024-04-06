package photosfx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


/**
 * Controller
 * @author Dhruv Shidhaye
 * @author Lakshya Gour
 */

public class Controller {

    @FXML
    private Label label;

    public void initialize() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
    }
}