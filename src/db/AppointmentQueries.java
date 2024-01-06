package db;

import objects.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import utils.Time;


public class AppointmentQueries {
    public static final String defaultFormat = "yyyy-MM-dd HH:mm:ss";
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(defaultFormat);

    /**
     * return a of list all appointments in table
     */
    public static List<Appointment> list() {

        String sql  = "SELECT * FROM client_schedule.appointments";
        List<Appointment> appointments = new ArrayList<>();

        try {

            ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();
            while(rs.next()) {
                Appointment toAdd = new Appointment(rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        Time.toSystem((LocalDateTime) rs.getObject("Start")),
                        Time.toSystem((LocalDateTime) rs.getObject("End")),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID")
                        );
                appointments.add(toAdd);

            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        return appointments;
    }

    /**
     * Insert record into appointment table
     */
   public static void insertAppointment(Appointment appt){
       String sql = "INSERT INTO client_schedule.appointments (Appointment_ID, Title, Description, Location, " +
               "Start, End, Customer_ID, User_ID, Contact_ID, Created_By, Last_Updated_By, Type, Create_Date, Last_Update)" +
               "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,NOW(),NOW())";
       try {
           PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
           ps.setInt(1, appt.getAppointmentId());
           ps.setString(2, appt.getTitle());
           ps.setString(3, appt.getDescription());
           ps.setString(4, appt.getLocation());
           ps.setString(5, utils.Time.toUTC((appt.getStart())).format(formatter));
           ps.setString(6, utils.Time.toUTC(appt.getEnd()).format(formatter));
           ps.setInt(7, appt.getCustomerId());
           ps.setInt(8, appt.getUserId());
           ps.setInt(9, appt.getContactId());
           ps.setString(10, "System");
           ps.setString(11, "System");
           ps.setString(12, appt.getType());
           System.out.println(ps.toString());
           ps.executeUpdate();

       } catch (SQLException e) {
           e.printStackTrace();
       }
   }

    /**
     * get data for specific appointment ID
     */
   public static Appointment getAppointment(int id){
        String sql = "SELECT * FROM client_schedule.appointments WHERE Appointment_ID = " + id;
       try {
           ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();
           if (rs.next()) {
               Appointment toReturn = new Appointment(
                       rs.getInt("Appointment_ID"),
                       rs.getString("Title"),
                       rs.getString("Description"),
                       rs.getString("Location"),
                       rs.getString("Type"),
                       (LocalDateTime) rs.getObject("Start"),
                       (LocalDateTime)rs.getObject("End"),
                       rs.getInt("Customer_ID"),
                       rs.getInt("User_ID"),
                       rs.getInt("Contact_ID")
               );
               System.out.println(toReturn.getAppointmentId());
               return toReturn;
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return null;
   }

    /**
     * get next available id in table
     */
    public static Integer getNextId(){
        String sql = "SELECT MAX(Appointment_ID) AS max FROM Appointments";
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
     * update appointment record
     */
    public static void updateAppointment(Appointment appt) {
        String sql = "UPDATE Appointments SET Title = ?, Description = ?, Location = ?, Start = ?, End = ?, " +
                "Customer_ID = ?, User_ID = ?, Contact_ID = ?, Last_Updated_By = ?, Type = ?, Last_Update = NOW()" +
                " WHERE Appointment_ID = ?";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, appt.getTitle());
            ps.setString(2, appt.getDescription());
            ps.setString(3, appt.getLocation());
            ps.setString(4, appt.getStart().format(formatter));
            ps.setString(5, appt.getEnd().format(formatter));
            ps.setInt(6, appt.getCustomerId());
            ps.setInt(7, appt.getUserId());
            ps.setInt(8, appt.getContactId());
            ps.setString(9, "System");
            ps.setString(10, appt.getType());
            ps.setInt(11, appt.getAppointmentId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * delete appointment record
     */
    public static void deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, appointmentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    /**
     * get appointment records for specific customer ID
     */
    public static List<Appointment> getCustomerAppointments(int customerId) {
        String sql = "SELECT * FROM client_schedule.appointments WHERE Customer_ID = " + customerId;
        List<Appointment> appointments = new ArrayList<>();

        try {
            ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();
            while(rs.next()) {
                Appointment toAdd = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        (LocalDateTime)rs.getObject("Start"),
                        (LocalDateTime)rs.getObject("End"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID")
                );
                appointments.add(toAdd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public static List<Integer> getAppointmentIDsFromCustomer(int customerID){
        List<Integer> toReturn = new ArrayList<>();
        String sql = "SELECT Appointment_ID FROM appointments WHERE Customer_ID = " + customerID;
        try {
            ResultSet rs = DBConnection.getConnection().prepareStatement(sql).executeQuery();
            while(rs.next()){
                toReturn.add(rs.getInt("Appointment_ID"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
