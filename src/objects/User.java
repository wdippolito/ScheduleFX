package objects;

/**
 * class for representing user data structure
 */

public class User {
    private String userName;
    private String password;
    private int userID;

    public User(int id, String userName) {
        this.userID = id;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }
}
