package db;

import objects.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerQueries {

    /**
     * List all customers from table
     */
    public static List<Customer> list(){
        String sql  = "SELECT * FROM client_schedule.customers";
        List<Customer> customers = new ArrayList<>();
        try {
            ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();
            while(rs.next()){
                Customer toAdd = new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Phone"),
                        rs.getString("Postal_Code"),
                        rs.getInt("Division_ID"),
                        DivisionQueries.getDivisionName(rs.getInt("Division_ID")),
                        CountryQueries.getCountryName(DivisionQueries.getCountryID(rs.getInt("Division_ID")))
                );
                customers.add(toAdd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
        }

    /**
     * get customer with specific ID
     */
    public static Customer getCustomer(int id){
        String sql = "SELECT * FROM client_schedule.customers WHERE Customer_ID = " + id;
        try {
            ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();
            if(rs.next()) {
                Customer toReturn = new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Phone"),
                        rs.getString("Postal_Code"),
                        rs.getInt("Division_ID"),
                        DivisionQueries.getDivisionName(rs.getInt("Division_ID")),
                        CountryQueries.getCountryName(DivisionQueries.getCountryID(rs.getInt("Division_ID")))
                );
                return toReturn;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * insert new customer record
     */
    public static void insertCustomer(String name, String address, String phone, String country, String division, String postalCode) {
        String sql = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID)" +
                " VALUES (?, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?)";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, getNextId());
            ps.setString(2, name);
            ps.setString(3, address);
            ps.setString(4, postalCode);
            ps.setString(5, phone);
            // ps.setString(6, "NOW()");
            ps.setString(6, "System");
            // ps.setString(8, NOW());
            ps.setString(7, "System");
            ps.setInt(8, db.DivisionQueries.getDivisionID(division));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * update existing customer record
     */
    public static void updateCustomer(int id, String name, String address, String phone, String country, String
            division, String postalCode){
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = NOW(), Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setString(5, "System");
            ps.setInt(6, db.DivisionQueries.getDivisionID(division));
            ps.setInt(7, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * get next available id in table
     */
    public static Integer getNextId(){
        String sql = "SELECT MAX(Customer_ID) AS max FROM Customers";
        try {
            ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();
            rs.next();
            return rs.getInt("max") + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * delete customer
     */
    public static void deleteCustomer(Integer id){
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
