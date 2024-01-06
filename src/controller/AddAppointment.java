package controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import objects.Appointment;
import objects.Contact;
import objects.Customer;
import objects.User;
import utils.SceneChange;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddAppointment implements Initializable {
    public DatePicker startDate;
    public DatePicker apptEndDate;
    public TextField apptID;
    public TextField apptTitle;
    public TextField apptType;
    public TextField apptDescription;
    public TextField apptLocation;
    public ChoiceBox startHour;
    public ChoiceBox startMinute;
    public ChoiceBox endHour;
    public ChoiceBox endMinute;
    public ObservableList<Integer> hours = FXCollections.observableArrayList();
    public ObservableList<Integer> minutes = FXCollections.observableArrayList();
    public ChoiceBox apptContact;
    public ChoiceBox userID;
    public ChoiceBox customerID;

    private ZoneId estZoneId = ZoneId.of("America/New_York");



    private ObjectProperty<LocalDateTime> dateTimeValue = new SimpleObjectProperty<>(LocalDateTime.now());
    public static final String defaultFormat = "yyyy-MM-dd HH:mm:ss";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(defaultFormat);

    /**
     * called when save button is clicked, verifies inputs and saves data and will return to mainscreen
     */
    public void onClickSave(ActionEvent actionEvent) {

        if(checkForEmptyFields()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Missing Fields");
            alert.setContentText("Please fill all fields");
            alert.showAndWait();
        }else{
            LocalDate beginDate = startDate.getValue();
            int hour = (int) startHour.getValue();
            int minute = (int) startMinute.getValue();
            LocalDateTime startDT = beginDate.atTime(hour, minute);
            LocalDate endDate =  apptEndDate.getValue();
            LocalDateTime endDT = endDate.atTime((int) endHour.getValue(), (int) endMinute.getValue());
            LocalTime eight = LocalTime.of(8,0);
            LocalTime ten = LocalTime.of(22,0);
            if(startDT.atZone(ZoneId.systemDefault()).withZoneSameInstant(estZoneId).toLocalTime().isBefore(eight)
                    || endDT.atZone(ZoneId.systemDefault()).withZoneSameInstant(estZoneId).toLocalTime().isAfter(ten)
            ){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Meeting Time error");
                alert.setContentText("Appointment Outside Business Hours");
                alert.showAndWait();

            }else {
                Appointment appt = new Appointment(
                        db.AppointmentQueries.getNextId(),
                        apptTitle.getText(),
                        apptDescription.getText(),
                        apptLocation.getText(),
                        apptType.getText(),
                        startDT,
                        endDT,
                        Character.getNumericValue(customerID.getValue().toString().charAt(0)),
                        Character.getNumericValue(userID.getValue().toString().charAt(0)),
                        Character.getNumericValue(apptContact.getValue().toString().charAt(0))
                );
                if (!verifyMeetingTime(startDT, endDT)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Meeting Time error");
                    alert.setContentText("Meeting start cannot be after meeting End");
                    alert.showAndWait();

                } else if (verifyConflicts(startDT, endDT, appt.getCustomerId())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Meeting Time error");
                    alert.setContentText("Cannot set overlapping meetings Customer");
                    alert.showAndWait();
                } else {
                    db.AppointmentQueries.insertAppointment(appt);
                    String resource = "/view/MainScreen.fxml";
                    SceneChange.loadPage(resource, actionEvent);
                }
            }
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
     * Initialize page and prepare lists for choice boxes
     *
     * This function uses three lamba expressions, one to form each of the lists for contacts, users and customers.
     * the db queries return a list of objects of each data type, the lambda expression streams and maps the id and
     * name attributes from each object into a concatenated String to be used for displaying the contacts, users and
     * customers in the selection boxes. this allows for better readability for the user in the GUI selecting the
     * correct items from the dropdown selection boxes.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        apptID.setText(String.valueOf(db.AppointmentQueries.getNextId()));
        System.out.println(dateTimeValue.getValue().format(formatter));
        LocalDateTime time =  LocalDateTime.of(2023, 06, 23, 3, 30);
        for (int i = 0; i <= 23; i++) {
            hours.add(i);
        }
        for (int i = 0; i <= 59; i++) {
            minutes.add(i);
        }
        ObservableList<String> contactList = FXCollections.observableArrayList(db.ContactQueries.list().stream()
                .map(Contact -> Contact.getId() + " - " + Contact.getName()).toList());
        ObservableList<String> userList = FXCollections.observableArrayList(db.UserQueries.list().stream()
                .map(User -> User.getUserID() + " - " + User.getUserName()).toList());
        ObservableList<String> CustomerList = FXCollections.observableArrayList(db.CustomerQueries.list().stream()
                .map(Customer -> Customer.getCustomerId() + " - " + Customer.getName()).toList());

        apptContact.setItems(contactList);
        userID.setItems(userList);
        customerID.setItems(CustomerList);
        startHour.setItems(hours);
        startMinute.setItems(minutes);
        endHour.setItems(hours);
        endMinute.setItems(minutes);

    }
    /**
     * returns true if empty fields are found
     */
    private boolean checkForEmptyFields() {
        if (apptTitle.getText().isEmpty() || apptType.getText().isEmpty() || apptLocation.getText().isEmpty()
                || apptDescription.getText().isEmpty() || customerID.getValue().toString().isEmpty()
                || userID.getValue().toString().isEmpty() || apptContact.getValue().toString().isEmpty()

        ){
            return true;
        }
        else {
            return false;
        }

    }
    /**
     * returns true if start and end times are valid
     */
    public boolean verifyMeetingTime(LocalDateTime start, LocalDateTime end){
        if (start.isAfter(end)){
            return false;
        }
        return true;
    }

    /**
     * looks for overlapping appointments, returns true one is found
     */
    private boolean verifyConflicts(LocalDateTime startNew, LocalDateTime endNew, int customerId) {
        List<Appointment> appts = db.AppointmentQueries.getCustomerAppointments(customerId);
        if(appts == null){
            return false;
        }
        for (int i = 0; i < appts.size(); i++){
            LocalDateTime endExisting = appts.get(i).getEnd();
            LocalDateTime startExisting = appts.get(i).getStart();
            if(startNew.isBefore(endExisting) && (endNew.isAfter(startExisting))){
                return true;
            }
        }
        return false;
    }
}
