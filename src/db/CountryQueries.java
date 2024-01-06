package db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryQueries {

    /**
     * get country name from country ID
     */
    public static String getCountryName(int countryId){
        String countryName = null;
        String sql = "SELECT Country From client_schedule.countries WHERE Country_ID = " + countryId;
        try {
            ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();
            rs.next();
            countryName = rs.getString("Country");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryName;
    }

    /**
     * list all country names
     */
    public static ObservableList getCountries(){
        String sql = "SELECT Country FROM client_schedule.countries";
        ObservableList<String> toReturn = FXCollections.observableArrayList();
        try {
            ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();
            while(rs.next()){
                toReturn.add(rs.getString("Country"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }
    /**
     * get country ID from name
     */
    public static Integer getCountryID(String countryName){
        String sql = "SELECT Country_ID FROM client_schedule.countries WHERE Country = \"" + countryName + "\"";
        try {
            ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();
            rs.next();
            return rs.getInt("Country_ID");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
