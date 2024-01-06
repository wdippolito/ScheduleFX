package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {

    private static final String protocol = "JDBC";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//localhost/";
    private static final String dbName = "client_schedule";

    private static final String username = "sqlUser";
    private static final String password = "Passw0rd!";

    private static final String dbCurl = protocol + vendorName + ipAddress + dbName;
    private static final String driver = "com.mysql.cj.jdbc.Driver";

    private static Connection conn = null;

    /**
     * start connection to db
     */
    public static Connection startConnection(){
        try {
            System.out.println(dbCurl);
            Class.forName(driver);

        } catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        } try {
            conn = DriverManager.getConnection(dbCurl, username, password);
            System.out.println("Connection Successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * return connection object
     */
    public static Connection getConnection(){
        return conn;
    }

    /**
     * close connection to db
     */
    public static void closeConnection() throws SQLException {
        conn.close();
    }
}
