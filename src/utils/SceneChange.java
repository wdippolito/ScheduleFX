package utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * class for handling scene/page changes across app
 */
public class SceneChange {

    public static void loadPage(String resource, ActionEvent actionEvent){
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Font font =  Font.font("Arial", 20);
        Parent scene = null;
        try {
            scene = FXMLLoader.load(SceneChange.class.getResource(resource));
            scene.setStyle("-fx-font-family: '" + font.getFamily() + "';");

        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
