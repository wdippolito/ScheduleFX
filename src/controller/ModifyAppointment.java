package controller;
import db.AppointmentQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import objects.Appointment;
import objects.User;
import utils.SceneChange;
import utils.Time;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;

public class ModifyAppointment implements Initializable{
    public static Integer id;
    public static Appointment selectedAppointment;
    @FXML
    public TextField apptID;
    @FXML
    public TextField apptTitle;
    @FXML
    public TextField apptType;
    @FXML
    public TextField apptDescription;
    @FXML
    public TextField apptLocation;
    @FXML
    public Button saveButton;
    @FXML
    public Button cancelButton;
    @FXML
    public ChoiceBox apptContact;
    @FXML
    public ChoiceBox apptUserId;
    @FXML
    public ChoiceBox apptCustId;
    @FXML
    public DatePicker apptStartDate;
    @FXML
    public DatePicker apptEndDate;
    @FXML
    public ChoiceBox apptStartHour;
    @FXML
    public ChoiceBox apptEndHour;
    @FXML
    public ChoiceBox apptStartMinute;
    @FXML
    public ChoiceBox apptEndMinute;
    @FXML
    public ObservableList<Integer> hours = FXCollections.observableArrayList();
    @FXML
    public ObservableList<Integer> minutes = FXCollections.observableArrayList();

    private ZoneId estZoneId = ZoneId.of("America/New_York");


    /**
     * populate fields with data from appointment selected for modification
     *
     *  this function uses three lamba expressions, one to form each of the lists for contacts, users and customers.
     *  the db queries return a list of objects of each data type, the lambda expression streams and maps the id and
     *  name attributes from each object into a concatenated String to be used for displaying the contacts, users and
     *  customers in the selection boxes. this allows for better readability for the user in the GUI selecting the
     * correct items from the dropdown selection boxes
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Appointment appt = db.AppointmentQueries.getAppointment(selectedAppointment.getAppointmentId());

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
        apptUserId.setItems(userList);
        apptCustId.setItems(CustomerList);
        apptStartHour.setItems(hours);
        apptStartMinute.setItems(minutes);
        apptEndHour.setItems(hours);
        apptEndMinute.setItems(minutes);

        apptID.setText(String.valueOf(appt.getAppointmentId()));
        apptTitle.setText(appt.getTitle());
        apptType.setText(appt.getType());
        apptDescription.setText(appt.getDescription());
        apptLocation.setText(appt.getLocation());
        apptStartDate.setValue(appt.getStart().toLocalDate());
        apptEndDate.setValue(appt.getEnd().toLocalDate());
        apptStartHour.setValue(appt.getStart().toLocalTime().getHour());
        apptStartMinute.setValue(appt.getStart().toLocalTime().getMinute());
        apptEndHour.setValue(appt.getEnd().toLocalTime().getHour());
        apptEndMinute.setValue(appt.getEnd().toLocalTime().getMinute());
        apptUserId.setValue(userList.get(appt.getUserId()-1));
        apptCustId.setValue(CustomerList.get(appt.getCustomerId()-1));
        apptContact.setValue(contactList.get(appt.getContactId()-1));

    }
    /**
     * verify inputs and save record
     */
    public void onClickSave(ActionEvent actionEvent) {
        if (checkForEmptyFields()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Missing Fields");
            alert.setContentText("Please fill all fields");
            alert.showAndWait();
        }else{
            LocalDate beginDate = apptStartDate.getValue();
            int hour = (int) apptStartHour.getValue();
            int minute = (int) apptStartMinute.getValue();
            LocalDateTime startDT = beginDate.atTime(hour, minute);
            LocalDate endDate =  apptEndDate.getValue();
            LocalDateTime endDT = endDate.atTime((int) apptEndHour.getValue(), (int) apptEndMinute.getValue());
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
                        selectedAppointment.getAppointmentId(),
                        apptTitle.getText(),
                        apptDescription.getText(),
                        apptLocation.getText(),
                        apptType.getText(),
                        startDT,
                        endDT,
                        Character.getNumericValue(apptCustId.getValue().toString().charAt(0)),
                        Character.getNumericValue(apptUserId.getValue().toString().charAt(0)),
                        Character.getNumericValue(apptContact.getValue().toString().charAt(0))

                );
                if (!verifyMeetingTime(startDT, endDT)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Meeting Time error");
                    alert.setContentText("Meeting start cannot be after meeting End");
                    alert.showAndWait();

                } else if (verifyConflicts(startDT, endDT, appt.getCustomerId(),appt.getAppointmentId())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Meeting Time error");
                    alert.setContentText("Cannot set overlapping meetings Customer");
                    alert.showAndWait();
                } else {
                    db.AppointmentQueries.updateAppointment(appt);
                    String resource = "/view/MainScreen.fxml";
                    SceneChange.loadPage(resource, actionEvent);
                }
            }
        }
    }
    /**
     * check for empty fields
     */
    private boolean checkForEmptyFields() {
        if (apptTitle.getText().isEmpty() || apptType.getText().isEmpty() || apptLocation.getText().isEmpty()
                || apptDescription.getText().isEmpty() || apptCustId.getValue().toString().isEmpty()
                || apptUserId.getValue().toString().isEmpty() || apptContact.getValue().toString().isEmpty()

        ){
            return true;
        }
        else {
            return false;
        }

    }

    /**
     * check for  any overlapping appointments
     */

    private boolean verifyConflicts(LocalDateTime startNew, LocalDateTime endNew, int customerId, int apptID) {
       List<Appointment> appts = db.AppointmentQueries.getCustomerAppointments(customerId);
       if(appts == null){
           return false;
       }
       for (int i = 0; i < appts.size(); i++){
           LocalDateTime endExisting = appts.get(i).getEnd();
           LocalDateTime startExisting = appts.get(i).getStart();
           if(startNew.isBefore(endExisting) && (endNew.isAfter(startExisting))
                   && appts.get(i).getAppointmentId() != apptID){
               return true;
           }
       }
       return false;
    }

    /**
     * load main page
     */
    public void onClickCancel(ActionEvent actionEvent) {
        String resource = "/view/MainScreen.fxml";
        SceneChange.loadPage(resource, actionEvent);
    }

    /**
     * ensure meeting start time is before end time
     */
    public boolean verifyMeetingTime(LocalDateTime start, LocalDateTime end){
        if (start.isAfter(end)){
            return false;
        }
        return true;
    }
}
