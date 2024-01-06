package controller;

import db.CountryQueries;
import db.DivisionQueries;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import objects.Customer;
import utils.SceneChange;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyCustomer implements Initializable {
    public static Customer selectedCustomer = null;
    public TextField custId;
    public TextField custName;
    public TextField custAddress;
    public TextField custPhone;
    public TextField custPostalCode;
    public ChoiceBox custCountry;
    public ChoiceBox custState;

    /**
     * verify fields and update customer record then load login page
     */
    public void onClickSave(ActionEvent actionEvent) {

        if(checkForEmptyFields()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Missing Fields");
            alert.setContentText("Please fill all fields");
            alert.showAndWait();
        }else {

            db.CustomerQueries.updateCustomer(
                    selectedCustomer.getCustomerId(),
                    custName.getText(),
                    custAddress.getText(),
                    custPhone.getText(),
                    String.valueOf(custCountry.getSelectionModel().getSelectedItem()),
                    String.valueOf(custState.getSelectionModel().getSelectedItem()),
                    custPostalCode.getText()
            );
            String resource = "/view/MainScreen.fxml";
            SceneChange.loadPage(resource, actionEvent);
        }
    }

    /**
     * load appointment modify page for select appointment
     */

    public void onClickCancel(ActionEvent actionEvent) {
        String resource = "/view/MainScreen.fxml";
        SceneChange.loadPage(resource, actionEvent);
    }

    /**
     * populate fields from existing user record
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        custId.setText(String.valueOf(selectedCustomer.getCustomerId()));
        custName.setText(selectedCustomer.getName());
        custAddress.setText(selectedCustomer.getAddress());
        custPhone.setText(selectedCustomer.getPhoneNumber());
        custPostalCode.setText(selectedCustomer.getPostalCode());
        custCountry.setItems(CountryQueries.getCountries());
        custCountry.setValue(selectedCustomer.getCountry());
        custState.setItems(DivisionQueries.getDivisions(CountryQueries.getCountryID(selectedCustomer.getCountry())));
        custState.setValue(selectedCustomer.getDivision());
        custCountry.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            custState.setItems(DivisionQueries.getDivisions(CountryQueries.getCountryID((String) newValue)));
            custState.setValue(null);
        });
    }

    /**
     * check for empty fields
     */
    public boolean checkForEmptyFields(){
        return (custCountry.getValue().toString().isEmpty() || custName.getText().isEmpty()
                || custAddress.getText().isEmpty() || custPhone.getText().isEmpty()
                || custState.getValue().toString().isEmpty() || custPostalCode.getText().isEmpty());
    }
}
