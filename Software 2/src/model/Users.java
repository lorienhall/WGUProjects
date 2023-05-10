package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;

import java.sql.*;

/** This is the class for the users. */
public class Users
{
    /** Attribute declarations. */
    private static ObservableList<Users> allUsers = FXCollections.observableArrayList();
    private int User_ID;
    private String User_Name;
    private String Password;
    private Timestamp Create_Date;
    private String Created_By;
    private Timestamp Last_Update;
    private String Last_Updated_By;

    /** Users constructor. */
    public Users (int User_ID, String User_Name, String Password, Timestamp Create_Date, String Created_By, Timestamp Last_Update, String Last_Updated_By)
    {
        this.User_ID = User_ID;
        this.User_Name = User_Name;
        this.Password = Password;
        this.Create_Date = Create_Date;
        this.Created_By = Created_By;
        this.Last_Update = Last_Update;
        this.Last_Updated_By = Last_Updated_By;
    }

    /** This method gets all the countries stored in the database. */
    public static ObservableList<Users> getUsersFromDB()
    {
        ObservableList<Users> cList = FXCollections.observableArrayList();
        try{
            // My sql statement
            String sql = "select * from users";

            // Create a prepared statement
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            // Execute query and get results
            ResultSet rs = ps.executeQuery();

            // Work through the result set one row at a time
            while (rs.next())
            {

                int userID = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                Timestamp Create_Date = rs.getTimestamp("Create_Date");
                String Created_By = rs.getString("Created_By");
                Timestamp Last_Update = rs.getTimestamp("Last_Update");
                String Last_Updated_By  = rs.getString("Last_Updated_By");

                Users users = new Users(userID, userName, password,Create_Date,Created_By,Last_Update,Last_Updated_By);
                cList.add(users);
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return cList;
    }

    /** This method gets all the users. */
    public static ObservableList<Users> getAllUsers() { return allUsers; }

    /** This method sets the allCountries list to what is found in the database. */
    public static void setAllUsers(ObservableList<Users> allUsers) { Users.allUsers = allUsers; }

    /** This method finds the matching user to the given name. */
    public static Users searchUsers(String user)
    {
        for(Users users : getAllUsers())
        {
            if (users.getUser_Name().equals(user))
            {
                return users;
            }
        }
        return null;
    }

    /** This method makes the user object return the user id and name if it is called for a combo box. */
    @Override
    public String toString()
    {
        return "[" + User_ID + "]" + " " + User_Name;
    }

    /** Getters and setters for the users attributes*/
    public int getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Timestamp getCreate_Date() {
        return Create_Date;
    }

    public void setCreate_Date(Timestamp create_Date) {
        Create_Date = create_Date;
    }

    public String getCreated_By() {
        return Created_By;
    }

    public void setCreated_By(String created_By) {
        Created_By = created_By;
    }

    public Timestamp getLast_Update() {
        return Last_Update;
    }

    public void setLast_Update(Timestamp last_Update) {
        Last_Update = last_Update;
    }

    public String getLast_Updated_By() {
        return Last_Updated_By;
    }

    public void setLast_Updated_By(String last_Updated_By) {
        Last_Updated_By = last_Updated_By;
    }
}
