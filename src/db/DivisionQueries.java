package db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DivisionQueries {

    /**
     * get division name by ID
     */
    public static String getDivisionName(int division_id){
        String sql  = "SELECT Division FROM client_schedule.first_level_divisions WHERE Division_ID = " + division_id;
        String divisionName = null;
        try {
            ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();
            rs.next();
            divisionName = rs.getString("division");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return divisionName;

    }

    /**
     * get country ID associated with Division ID
     */
    public static Integer getCountryID(int division_id){
        String sql  = "SELECT Country_ID FROM client_schedule.first_level_divisions WHERE Division_ID = " + division_id;
        Integer countryId = null;
        try {
            ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();
            rs.next();
            countryId = rs.getInt("Country_ID");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(countryId);
        return countryId;
    }

    /**
     * get divisions in country
     */
    public static ObservableList getDivisions(int countryId) {
        String sql = "SELECT Division From first_level_divisions WHERE Country_ID = " + countryId;
        ObservableList<String> toReturn = FXCollections.observableArrayList();
        try {
            ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();
            while (rs.next()) {
                toReturn.add(rs.getString("Division"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    /**
     * get division ID from name
     */
    public static Integer getDivisionID(String divisionName){
        String sql = "SELECT Division_Id FROM first_level_divisions WHERE Division = \"" + divisionName + "\"";
        Integer toReturn = null;
        try {
            ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();
            rs.next();
            toReturn = rs.getInt("Division_ID");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
