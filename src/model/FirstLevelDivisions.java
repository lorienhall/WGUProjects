package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/** This is the class for the first level divisions. */
public class FirstLevelDivisions
{
    /** Attribute declarations. */
    private static ObservableList <FirstLevelDivisions> firstLevelD = FXCollections.observableArrayList();
    private int Division_ID;
    private String Division;
    private Timestamp Create_Date;
    private String Created_By;
    private Timestamp Last_Update;
    private String Last_Updated_By;
    private int COUNTRY_ID;

    /** First level divisions constructor. */
    private FirstLevelDivisions ( int Division_ID, String Division, Timestamp Create_Date, String Created_By,
                                  Timestamp Last_Update, String Last_Updated_By, int COUNTRY_ID)
    {
        this.Division_ID = Division_ID;
        this.Division = Division;
        this.Create_Date = Create_Date;
        this.Created_By = Created_By;
        this.Last_Update = Last_Update;
        this.Last_Updated_By = Last_Updated_By;
        this.COUNTRY_ID = COUNTRY_ID;

    }

    /** This method gets all the divisions. */
    public static ObservableList<FirstLevelDivisions> getFirstLevelD() { return firstLevelD; }

    /** This method gets all the divisions based on the country. */
    public static ObservableList<FirstLevelDivisions> getFirstLDByCountry (Country country)
    {
        ObservableList<FirstLevelDivisions> clist = FXCollections.observableArrayList();
        for (FirstLevelDivisions firstLevelDivisions : getFirstLevelD())
        {
            if (country.getCountry_ID() == firstLevelDivisions.getCOUNTRY_ID())
            {
                clist.add(firstLevelDivisions);
            }
        }
        return clist;
    }

    /** This method finds the matching first level division to the given name. */
    public static model.FirstLevelDivisions searchFirstLevel (String firstLevel)
    {
        for(FirstLevelDivisions f : getFirstLevelD())
        {
            if (f.getDivision().equals(firstLevel))
            {
                return f;
            }
        }
        return null;
    }

    /** This method gets all the divisions stored in the database. */
    public static ObservableList <FirstLevelDivisions> getFirstLevelDFromDB()
    {
        ObservableList<FirstLevelDivisions> cList = FXCollections.observableArrayList();
        try{
            // My sql statement
            String sql = "select * from first_level_divisions";

            // Create a prepared statement
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            // Execute query and get results
            ResultSet rs = ps.executeQuery();

            // Work through the result set one row at a time
            while (rs.next())
            {

                int Division_ID = rs.getInt("Division_ID");
                String Division = rs.getString("Division");
                Timestamp Create_Date = rs.getTimestamp("Create_Date");
                String Created_By = rs.getString("Created_By");
                Timestamp Last_Update = rs.getTimestamp("Last_Update");
                String Last_Updated_By  = rs.getString("Last_Updated_By");
                int COUNTRY_ID = rs.getInt("COUNTRY_ID");

                FirstLevelDivisions firstLevelDivisions = new FirstLevelDivisions(Division_ID,Division,Create_Date,Created_By,Last_Update,Last_Updated_By, COUNTRY_ID);
                cList.add(firstLevelDivisions);
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return cList;
    }
    /** This method sets the allCustomers list to what is found in the database. */
    public static void setAllDivisions(ObservableList<FirstLevelDivisions> firstLevelD) {
        FirstLevelDivisions.firstLevelD = firstLevelD;
    }

    /** This method makes the first level division object return the first level division id and name if it is called for a combo box. */
    @Override
    public String toString()
    {
        return "[" + Division_ID + "] " + Division;
    }

    /** Getters and setters for the attributes. */
    public int getDivision_ID() {
        return Division_ID;
    }

    public void setDivision_ID(int division_ID) {
        Division_ID = division_ID;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        Division = division;
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

    public int getCOUNTRY_ID() {
        return COUNTRY_ID;
    }

    public void setCOUNTRY_ID(int COUNTRY_ID) {
        this.COUNTRY_ID = COUNTRY_ID;
    }
}
