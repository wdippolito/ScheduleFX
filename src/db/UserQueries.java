package db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import objects.Contact;
import objects.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserQueries {


    /**
     * query for matching username and password and return true if found, false if not.
     */
    public static boolean validateUser(String username, String password){
        String sql = "SELECT * From client_schedule.users Where user_name = \"" + username + "\" && password = \"" + password + "\"";
        try {
            ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * list all users
     */
    public static ObservableList<User> list(){
        String sql = "SELECT * FROM Users";
        ObservableList<User> toReturn = FXCollections.observableArrayList();
        try {
            ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();
            while(rs.next()){
                User toAdd = new User(rs.getInt("User_ID"),
                        rs.getString("User_Name"));
                toReturn.add(toAdd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }
    /**
     * get ID from name
     */
    public static int getIdFromName(String userName){
       String sql = "SELECT User_ID FROM users WHERE User_Name = \"" + userName + "\"";
       System.out.println(sql);
        try {
            ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();
            if(rs.next()) {
                return rs.getInt("User_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
