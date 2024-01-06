package controller;

import db.CustomerQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import objects.Appointment;
import db.AppointmentQueries;
import objects.Customer;
import utils.SceneChange;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class MainScreen implements Initializable {

    public static String loggedInUser;

    @FXML
    public TableView<Appointment> appointmentTable;

    @FXML
    public TableView<Appointment> apptTable;

    @FXML
    public TableColumn<Appointment, Integer> apptID;

    @FXML
    public TableColumn<Appointment, String> apptTitle;

    @FXML
    public TableColumn<Appointment, String> apptDescription;

    @FXML
    public TableColumn<Appointment, String> apptLocation;

    @FXML
    public TableColumn<Appointment, String> apptContact;

    @FXML
    public TableColumn<Appointment, String> apptType;

    @FXML
    public TableColumn<Appointment, String> appsStart;

    @FXML
    public TableColumn apptEnd;

    @FXML
    public TableColumn apptCustomerID;

    @FXML
    public TableColumn apptUserID;


    public TableView<Customer> custTable;
    @FXML
    public TableColumn<Customer, Integer> custID;
    @FXML
    public TableColumn<Customer, String> custName;
    @FXML
    public TableColumn<Customer, String>  custAddress;
    @FXML
    public TableColumn<Customer, String>  custPhoneNumber;
    @FXML
    public TableColumn<Customer, String>  custState;
    @FXML

    public TableColumn<Customer, String>  custPostalCode;
    @FXML
    public TableColumn<Customer, String>  custDivision;
    @FXML
    public Button addAppt;
    @FXML
    public Button modifyAppt;
    @FXML
    public Button deleteAppt;
    @FXML
    public Button addCust;
    @FXML
    public Button modifyCust;
    @FXML
    public Button deleteCust;
    @FXML
    public Button logout;
    @FXML
    public Button reports;
    @FXML
    public TableColumn custCountry;

    public Label apptAlert;
    public ToggleButton toggleView;
    public RadioButton monthly;
    public ToggleGroup group;
    public RadioButton weekly;
    public RadioButton all;

    private String nextAppointment;
    private static ObservableList<Appointment> filtered;


    @Override

    /**
     * set appointment alert if relevant, display appointment and customer tables.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        apptAlert.setText(checkForAppointments(loggedInUser));
        displayApptTable(AppointmentQueries.list());
        displayCustTable(CustomerQueries.list());
    }

    /**
     * checks if the logged in user has any upcoming appointments in 15 mins
     */
    private String checkForAppointments(String loggedInUser) {

        List<Appointment> filteredAppointments = AppointmentQueries.list();
        for (int i = 0; i < filteredAppointments.size(); i++){
           int userId =  filteredAppointments.get(i).getUserId();
           if(userId == db.UserQueries.getIdFromName(loggedInUser)
                  && filteredAppointments.get(i).getStart().minusMinutes(15).isBefore(LocalDateTime.now())
                  && filteredAppointments.get(i).getStart().isAfter(LocalDateTime.now())
           ){
               System.out.println(userId + " " + filteredAppointments.get(i).getAppointmentId()+ " " + filteredAppointments.get(i).getStart());
               nextAppointment = "Appointment ID: " + filteredAppointments.get(i).getAppointmentId() + " will be at "
                       + filteredAppointments.get(i).getStart().toLocalTime() + " on " + filteredAppointments.get(i).getStart().toLocalDate();
               return nextAppointment;
           }
        }
        return "No Upcoming Appointments";
    }

    /**
     * display appointment data in tableview
     *
     * this function uses a lambda function to implement the weekly and monthly filtering for appointments, if the
     * monthly filter is selected appointment objects within a 4 week window are collected for display, if weekly
     * is selected it is a one week window.
     */
    private void displayApptTable(List<Appointment> appointmentList){
        ObservableList<Appointment> observableList = FXCollections.observableArrayList(appointmentList);
        setApppointmentFactory(observableList);
        all.setOnAction(event -> {
            if(all.isSelected()) {
                setApppointmentFactory(observableList);
            }
        }
        );
        weekly.setOnAction(event -> {
            if (weekly.isSelected()) {
                // Switch to weekly view
               filtered = observableList.stream()
                        .filter(appointment -> (appointment.getStart().toLocalDate().isBefore(LocalDate.now().plusWeeks(1)))
                                && appointment.getStart().toLocalDate().isAfter(LocalDate.now().minusDays(1)))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                setApppointmentFactory(filtered);
                // Implement your logic for displaying monthly appointments
            }
        });
        monthly.setOnAction(event -> {
            if(monthly.isSelected()){
                // Switch to monthly view
                filtered = observableList.stream()
                        .filter(appointment -> (appointment.getStart().isBefore(LocalDateTime.now().plusWeeks(4)))
                                && appointment.getStart().toLocalDate().isAfter(LocalDate.now().minusDays(1)))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                setApppointmentFactory(filtered);
                // Implement your logic for displaying weekly appointments
            }
        });
    }
    /**
     * set data sources for each column in table view
     */
    public void setApppointmentFactory(ObservableList<Appointment> appointments){
        apptTable.setItems(appointments);
        apptID.setCellValueFactory(new PropertyValueFactory<> ("appointmentId") );
        apptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptContact.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        apptType.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        apptUserID.setCellValueFactory(new PropertyValueFactory<>("userId"));
        appsStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        apptEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
    }

    /**
     * set data sources for each column in table view for customer info
     */
    private void displayCustTable(List<Customer> customerList){
        ObservableList<Customer> observableList = FXCollections.observableArrayList(customerList);
        custTable.setItems(observableList);
        custID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        custName.setCellValueFactory(new PropertyValueFactory<>("name"));
        custAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        custPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        custPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        custDivision.setCellValueFactory(new PropertyValueFactory<>("division"));
        custCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
    }

    /**
     * load appointment add page
     */
    public void onAddApptClick(ActionEvent actionEvent) {
        String resource = "/view/AddAppointment.fxml";
        SceneChange.loadPage(resource, actionEvent);
    }
    /**
     * load appointment modify page for select appointment
     */
    public void onModifyApptClick(ActionEvent actionEvent) {
        if(apptTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Selection");
            alert.setContentText("Please Select an Appointment Record to Modify");
            alert.showAndWait();
        }else {
            ModifyAppointment.selectedAppointment = apptTable.getSelectionModel().getSelectedItem();
            String resource = "/view/ModifyAppointment.fxml";
            SceneChange.loadPage(resource, actionEvent);
        }
    }
    /**
     * delete appointment and prompt for confirmation
     */
    public void onDeleteApptClick(ActionEvent actionEvent) {
        if (apptTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Selection");
            alert.setContentText("Please Select an Appointment Record to Delete");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Appointment");
            alert.setContentText("Are you sure you want to delete Appointment number " + apptTable.getSelectionModel().getSelectedItem().getAppointmentId());
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    // User clicked OK, perform delete operation
                    db.AppointmentQueries.deleteAppointment(apptTable.getSelectionModel().getSelectedItem().getAppointmentId());
                    displayApptTable(AppointmentQueries.list());
                } else {
                    // User clicked Cancel or closed the dialog
                }
            });
        }
    }

    /**
     * load customer add page
     */

    public void onAddCustClick(ActionEvent actionEvent) {
        String resource = "/view/AddCustomer.fxml";
        SceneChange.loadPage(resource, actionEvent);

    }
    /**
     * load customer modify page for selected customer
     */
    public void onAddModifyCustClick(ActionEvent actionEvent) {
        if(custTable.getSelectionModel().getSelectedItem() == null){
            //controller.Login.bundle = ResourceBundle.getBundle("messages",locale);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Selection");
            alert.setContentText("Please Select a Customer Record to Modify");
            alert.showAndWait();
        }else {
            ModifyCustomer.selectedCustomer = custTable.getSelectionModel().getSelectedItem();
            String resource = "/view/ModifyCustomer.fxml";
            SceneChange.loadPage(resource, actionEvent);
        }
    }

    /**
     * delete customer
     */
    public void onDeleteCustClick(ActionEvent actionEvent) {
        if (custTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Selection");
            alert.setContentText("Please Select a Customer Record to Delete");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer");
            alert.setContentText("Are you sure you want to delete this record?");
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    // User clicked OK, perform delete operation
                    int customerID = custTable.getSelectionModel().getSelectedItem().getCustomerId();
                    deleteAssociatedAppointments(customerID);
                    db.CustomerQueries.deleteCustomer(customerID);
                    displayCustTable(CustomerQueries.list());
                    displayApptTable(AppointmentQueries.list());
                    Alert alertDeleted = new Alert(Alert.AlertType.INFORMATION);
                    alertDeleted.setTitle("Customer Deleted");
                    alertDeleted.setContentText("Customer ID No. " + customerID + " and their Appointments were deleted");
                    alertDeleted.showAndWait();
                } else {
                    // User clicked Cancel or closed the dialog
                }
            });


        }
    }

    private void deleteAssociatedAppointments(int customerId) {
        for (Integer appointmentID:
        AppointmentQueries.getAppointmentIDsFromCustomer(customerId)) {
            db.AppointmentQueries.deleteAppointment(appointmentID);
        }
        
    }

    /**
     * load login screen, set loginuser to null
     */
    public void onLogoutClick(ActionEvent actionEvent) {
        loggedInUser = null;
        String resource = "/view/Login.fxml";
        SceneChange.loadPage(resource, actionEvent);

    }

    /**
     * load reports page
     */
    public void onReportClick(ActionEvent actionEvent) {
        String resource = "/view/Reports.fxml";
        SceneChange.loadPage(resource, actionEvent);

    }
}
