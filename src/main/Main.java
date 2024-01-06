package main;

import db.AppointmentQueries;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import db.DBConnection;

import java.sql.SQLException;

public class Main extends Application {

    /**
     * Entry point for application to load the Login screen
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        Font font =  Font.font("Arial", 20);
        root.setStyle("-fx-font-family: '" + font.getFamily() + "';");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    /**
     * main method, establishes and closes db connections and launches first page
     * @throws Exception
     */
    public static void main(String[] args) throws SQLException {
        DBConnection.startConnection();
        AppointmentQueries.list();
        launch(args);
        DBConnection.closeConnection();

    }
}
