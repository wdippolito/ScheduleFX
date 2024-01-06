package controller;

import db.CountryQueries;
import db.CustomerQueries;
import db.DivisionQueries;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.SceneChange;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomer implements Initializable {

    @FXML
    public TextField customerId;
    @FXML
    public TextField name;
    @FXML
    public TextField address;
    @FXML
    public TextField phoneNumber;
    @FXML
    public TextField postalCode;
    @FXML
    public ChoiceBox country;
    @FXML
    public ChoiceBox state;
    /**
     * called when save button is clicked, verifies inputs and saves data and will return to mainscreen
     */
    public void onClickSave(ActionEvent actionEvent) {
        if(checkForEmptyFields()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Missing Fields");
            alert.setContentText("Please fill all fields");
            alert.showAndWait();
        }else {
            db.CustomerQueries.insertCustomer(
                    name.getText(),
                    address.getText(),
                    phoneNumber.getText(),
                    String.valueOf(country.getSelectionModel().getSelectedItem()),
                    String.valueOf(state.getSelectionModel().getSelectedItem()),
                    postalCode.getText()
            );
            String resource = "/view/MainScreen.fxml";
            SceneChange.loadPage(resource, actionEvent);
        }
    }
    /**
     * called when cancel button is clicked, will return to mainscreen
     */
    public void onClickCancel(ActionEvent actionEvent) {
        String resource = "/view/MainScreen.fxml";
        SceneChange.loadPage(resource, actionEvent);

    }
    /**
     * initializes choice boxes and sets selected item properties to change state list based on selected country
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerId.setText(String.valueOf(CustomerQueries.getNextId()));
        country.setItems(CountryQueries.getCountries());
        country.setValue("U.S");
        state.setItems(DivisionQueries.getDivisions(CountryQueries.getCountryID("U.S")));
        country.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            state.setItems(DivisionQueries.getDivisions(CountryQueries.getCountryID((String) newValue)));
            state.setValue(null);
        });
    }
    /**
     * returns true if empty fields are detected
     */
    public boolean checkForEmptyFields(){
       return (country.getValue().toString().isEmpty() || name.getText().isEmpty()
                || address.getText().isEmpty() || phoneNumber.getText().isEmpty()
                || state.getValue().toString().isEmpty() || postalCode.getText().isEmpty());
    }
}
