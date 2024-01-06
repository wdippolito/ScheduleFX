package controller;

import db.UserQueries;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import objects.User;
import utils.SceneChange;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class Login implements Initializable {

    public ResourceBundle bundle = ResourceBundle.getBundle("messages");
    private static String logLocataion =  "login_activity.txt";
    public PasswordField password;
    public TextField username;
    public Button loginButton;
    public Label instructionLabel;
    public Button exitButton;
    public Label timezoneLabel;
    public Label timeZoneDisplay;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLocale();
        System.out.println(ZoneId.systemDefault().getId());
    }

    /**
     * attempts authentication by checking user table in db, if successful app main page
     */
    public void onLoginButtonClick(ActionEvent actionEvent) {
        writeToLog(username.getText(), LocalDateTime.now());
        if(authenticate(username.getText(),password.getText())){
            MainScreen.loggedInUser = username.getText();
            String resource = "/view/MainScreen.fxml";
            SceneChange.loadPage(resource, actionEvent);
        }else{
            passwordAlert(actionEvent);

        }

    }

    /**
     * opens filewriter to write login attempts to a log file.
     */
    private void writeToLog(String username, LocalDateTime timeNow) {
        String logMessage = username + " " + timeNow + "\n";
        try {
            FileWriter writer = new FileWriter(logLocataion, true);
            writer.write(logMessage);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * sets text sources from resource bundle
     */
    public void setLocale(){
        ZoneId zoneId = ZoneId.systemDefault();

        Locale locale = determineLocale(zoneId);
        bundle = ResourceBundle.getBundle("messages",locale);
        timeZoneDisplay.setText(zoneId.getId());
        username.setPromptText(bundle.getString("USERNAME_FIELD"));
        password.setPromptText(bundle.getString("PASSWORD_FIELD"));
        loginButton.setText(bundle.getString("LOGIN_BUTTON"));
        instructionLabel.setText(bundle.getString("INSTRUCTION_LABEL"));
        timezoneLabel.setText(bundle.getString("TIME_ZONE"));
        exitButton.setText(bundle.getString("EXIT_BUTTON"));
    }

    /**
     * determines whether system zone is Paris, sets language accordingly
     */
    private Locale determineLocale(ZoneId zoneId) {
        Locale defaultLocale = Locale.getDefault();
        if( defaultLocale.getLanguage().equals("fr") || zoneId.getId().contains("Paris")){
            return Locale.FRANCE;
        }else{
            return Locale.ENGLISH;
        }
    }

    /**
     * popup alert for bad password or username
     */
    public void passwordAlert(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString("LOGIN_BAD_CREDENTIALS"));
        alert.setContentText(bundle.getString("ALERT_TRY_AGAIN"));
        alert.showAndWait();
    }

    /**
     * exit app
     */
    public void onExitButtonClick(ActionEvent actionEvent) {
        System.exit(0);
    }


    public boolean authenticate(String username, String password){
        System.out.println((isUserValid(username,password)));
        return (isUserValid(username,password));

        //return false;

    }
    /**
     * calls db query to confirm user password combo
     */
    public boolean isUserValid(String username, String password){
        return UserQueries.validateUser(username, password);

    }

}
