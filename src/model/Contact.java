package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This the class for the contacts. */
public class Contact
{
    /** Attribute declarations. */
    private static ObservableList <Contact> allContacts = FXCollections.observableArrayList();
    private int conid;
    private String name;
    private String email;

    /** Contact constructor. */
    private Contact (int conid, String name, String email)
    {
        this.conid = conid;
        this.name = name;
        this.email = email;
    }

    /** This method adds a contact. */
    public static void addContact(Contact contact) { allContacts.add(contact); }
    /** This method gets all the appointments. */
    public static ObservableList<Contact> getAllContacts() { return allContacts;}

    /** This method gets all the contacts stored in the database. */
    public static ObservableList <Contact> getContactsFromDB()
    {
        ObservableList<Contact> cList = FXCollections.observableArrayList();
        try{
            // My sql statement
            String sql = "select * from contacts";

            // Create a prepared statement
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            // Execute query and get results
            ResultSet rs = ps.executeQuery();

            // Work through the result set one row at a time
            while (rs.next())
            {
                int contactid = rs.getInt("Contact_ID");
                String contactN = rs.getString("Contact_Name");
                String email = rs.getString("Email");


                Contact contact= new Contact(contactid,contactN,email);
                cList.add(contact);
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return cList;
    }

    /** This method sets the observable list to the provided list of appointments. */
    public static void setAllContacts(ObservableList<Contact> allContacts) {
        Contact.allContacts = allContacts;
    }

    /** This method finds the matching contact to the given name. */
    public static Contact searchContacts(String con)
    {
        for(Contact contact : getAllContacts())
        {
            if (contact.getName().equals(con))
            {
                return contact;
            }
        }
        return null;
    }

    /** This method makes the contact object return the contact id and name if it is called for a combo box. */
    @Override
    public String toString()
    {
        return "[" + conid + "]" + " " + name;
    }

    /** Getters and setters for the contact attributes. */
    public int getConid() {
        return conid;
    }

    public void setConid(int conid) {
        this.conid = conid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
