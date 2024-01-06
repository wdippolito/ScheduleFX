package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import objects.Appointment;
import utils.SceneChange;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Reports implements Initializable {

    public ChoiceBox contactChoice;
    public Button back;
    public TableView reportByContact;
    public TableColumn apptID;
    public TableColumn apptTitle;
    public TableColumn apptLocation;
    public TableColumn apptDescription;
    public TableColumn apptContact;
    public TableColumn apptType;
    public TableColumn appsStart;
    public TableColumn apptEnd;
    public TableColumn apptCustomerID;
    public TableColumn apptUserID;
    public TableView monthTypeTable;
    public TableColumn monthCol;
    public TableColumn typeCol;
    public TableColumn countCol;
    public TableView custByCountry;
    public TableColumn countryCol;
    public TableColumn customerCol;

    /**
     * return to main screen
     */
    public void onBackClick(ActionEvent actionEvent) {
        String resource = "/view/MainScreen.fxml";
        SceneChange.loadPage(resource, actionEvent);
    }
    /**
     * load reports
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayContactAppointment();
        displayMonthTypeCount();
        displayCustCountryReport();
    }

    /**
     * counts and displays appointment counts by month and type.
     */
    private void displayMonthTypeCount() {
        ObservableList<Appointment> appts = FXCollections.observableArrayList(db.AppointmentQueries.list());
        ObservableList<ReportFields> reportList = FXCollections.observableArrayList();
        Map<String, Integer> count = new HashMap<>();
        for (int i = 0; i < appts.size(); i++) {
            String key = appts.get(i).getStart().toLocalDate().getMonth() + ":" + appts.get(i).getType();
            if (count.containsKey(key)) {
                count.put(key, count.get(key) + 1);
            } else {
                count.put(key, 1);
            }
        }
        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            String[] keys = entry.getKey().split(":");
            int counts = entry.getValue();
            reportList.add(
                    new ReportFields(keys[0], keys [1], counts)
            );
            monthTypeTable.setItems(reportList);
            monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            countCol.setCellValueFactory(new PropertyValueFactory<>("numberOf"));
        }
    }
    /**
     * counts and displays customers by country
     */
    public void displayCustCountryReport(){
        List<String> countries = db.CustomerQueries.list().stream().map(customer -> customer.getCountry()).toList();
        Map<String, Integer> occurrences = new HashMap<>();
        ObservableList<countryCountFields> reportList = FXCollections.observableArrayList();

        for (String item: countries) {
            if (occurrences.containsKey(item)) {
                occurrences.put(item, occurrences.get(item) + 1);
            }else{
                occurrences.put(item, 1);
            }
        }
        for (Map.Entry<String, Integer> entry : occurrences.entrySet()) {
            reportList.add(new countryCountFields(entry.getKey(), entry.getValue()));
        }
        custByCountry.setItems(reportList);
        countryCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerCount"));
    }

    /**
     * class for representing report data
     */
    public class countryCountFields{
        private final String countryName;
        private final int customerCount;

        public countryCountFields(String countryName, int customerCount) {
            this.countryName = countryName;
            this.customerCount = customerCount;
        }

        public int getCustomerCount() {
            return customerCount;
        }

        public String getCountryName() {
            return countryName;
        }
    }

    /**
     * class for representing report data
     */
    public class ReportFields{

            private String month;
            private String type;
            private int numberOf;

        public ReportFields(String month, String type, int numberOf){
            this.month = month;
            this.type = type;
            this.numberOf = numberOf;
        }

            public String getMonth() {
                return month;
            }

            public String getType() {
                return type;
            }

            public int getNumberOf() {
                return numberOf;
            }


        }

    /**
     * set data sources for contact report
     */
    public void setValueFactory(ObservableList<Appointment> appts){
        reportByContact.setItems(appts);
        apptID.setCellValueFactory(new PropertyValueFactory<>("appointmentId") );
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
     * get and display reports for selected contact
     */
    public void displayContactAppointment(){
        ObservableList<String> contactList = FXCollections.observableArrayList(db.ContactQueries.list().stream()
                .map(Contact -> Contact.getId() + " - " + Contact.getName()).toList());
        contactChoice.setItems(contactList);
        contactChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            ObservableList<Appointment> appts = db.AppointmentQueries.list().stream().filter(appointment -> appointment.getContactId()
                            == Character.getNumericValue(contactChoice.getSelectionModel().getSelectedItem().toString().charAt(0)))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            setValueFactory(appts);
        });
        db.AppointmentQueries.list();

        }
}
