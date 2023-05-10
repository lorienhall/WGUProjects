package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/** This is the class for the countries. */
public class Country
{
    /** Attribute declarations. */
    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();
    private int Country_ID;
    private String Country;
    private Timestamp Create_Date;
    private String Created_By;
    private Timestamp Last_Update;
    private String Last_Updated_By;

    /** First level divisions constructor. */
    private Country ( int Country_ID, String Country, Timestamp Create_Date, String Created_By,
                                  Timestamp Last_Update, String Last_Updated_By)
    {
        this.Country_ID = Country_ID;
        this.Country = Country;
        this.Create_Date = Create_Date;
        this.Created_By = Created_By;
        this.Last_Update = Last_Update;
        this.Last_Updated_By = Last_Updated_By;
    }

    /** This method gets all the countries. */
    public static ObservableList<Country> getAllCountries() { return allCountries; }

    public static model.Country searchCountryS(String country)
    {
        for(Country c : getAllCountries())
        {
            if (c.getCountry().equals(country))
            {
                return c;
            }
        }
        return null;
    }

    /** This method gets all the countries stored in the database. */
    public static ObservableList <Country> getCountriesFromDB()
    {
        ObservableList<Country> cList = FXCollections.observableArrayList();
        try{
            // My sql statement
            String sql = "select * from countries";

            // Create a prepared statement
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            // Execute query and get results
            ResultSet rs = ps.executeQuery();

            // Work through the result set one row at a time
            while (rs.next())
            {

                int Country_ID = rs.getInt("Country_ID");
                String Country = rs.getString("Country");
                Timestamp Create_Date = rs.getTimestamp("Create_Date");
                String Created_By = rs.getString("Created_By");
                Timestamp Last_Update = rs.getTimestamp("Last_Update");
                String Last_Updated_By  = rs.getString("Last_Updated_By");

                Country country = new Country(Country_ID, Country,Create_Date,Created_By,Last_Update,Last_Updated_By);
                cList.add(country);
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return cList;
    }
    /** This method sets the allCountries list to what is found in the database. */
    public static void setAllCountries(ObservableList<Country> countries) {
        model.Country.allCountries = countries;
    }

    /** This method makes the contact object return the contact id and name if it is called for a combo box. */
    @Override
    public String toString()
    {
        return "[" + Country_ID + "] " + Country;
    }

    /** Getters and setters for the contact attributes. */
    public int getCountry_ID() {
        return Country_ID;
    }

    public void setCountry_ID(int country_ID) {
        Country_ID = country_ID;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
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
