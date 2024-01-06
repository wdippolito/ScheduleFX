package db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import objects.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactQueries {

    /**
     * list all contacts
     */

    public static ObservableList<Contact> list(){
        String sql = "SELECT * FROM Contacts";
        ObservableList<Contact> toReturn = FXCollections.observableArrayList();
        try {
            ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();
            while(rs.next()){
                Contact toAdd = new Contact(rs.getInt("Contact_ID"),
                        rs.getString("Contact_Name"),
                        rs.getString("Email"));
                toReturn.add(toAdd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
