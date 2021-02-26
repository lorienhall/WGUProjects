package model;

import java.util.ArrayList;
import java.util.List;

public class Account
{
    private String username;
    private static String password;
    private static List<String> users = new ArrayList<>();

    /** Accounts constructor. */
    public Account(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    /** Make the account object print the username and password if called. */
    @Override
    public String toString()
    {
        return (username + ", " + password);
    }

    /** Getters and Setters for the account attributes. */
    public static List<String> getUsers() { return users; }
    public static void setUsers(String user) {users.add(user);}

    public static String getPassFromUser(String username) {return password;}

    public String getUsername() { return username; }

    public String getPassword() { return password; }
}
