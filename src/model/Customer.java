package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;

import java.sql.*;

/** This is the class for the customers. */
public class Customer
{
    /** Attribute declarations. */
    private static ObservableList <Customer> allCustomers = FXCollections.observableArrayList();
    private int custid;
    private String fullName;
    private String address;
    private String country;
    private String firstLevel;
    private String postalCode;
    private String phoneNumber;
    private Timestamp Create_Date;
    private String Created_By;
    private Timestamp Last_Update;
    private String Last_Updated_By;

    /** Customers constructor. */
    public Customer (int custid, String fullName, String address, String country, String firstLevel,
                     String postalCode, String phoneNumber, Timestamp Create_Date, String Created_By,
                     Timestamp Last_Update, String Last_Updated_By)
    {
        this.custid = custid;
        this.fullName = fullName;
        this.address = address;
        this.country = country;
        this.firstLevel = firstLevel;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.Create_Date = Create_Date;
        this.Created_By = Created_By;
        this.Last_Update = Last_Update;
        this.Last_Updated_By = Last_Updated_By;
    }

    /** This method deletes a given customer from both the database and the observable list. */
    public static void deleteCustomer (Customer customer)
    {
        try {
            int custID = customer.getCustid();
            // Delete customer from the database
            String delete = "delete from customers where Customer_ID = " + custID + ";";
            PreparedStatement psd = DBConnection.getConnection().prepareStatement(delete);
            psd.execute();

            // Delete the customer from the observable list
            allCustomers.remove(customer);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /** This method makes a new customer. */
    public static void newCustomer(String fullName, String address, String country, String firstLevel,
                                   String postalCode, String phoneNumber, Timestamp Create_Date,
                                   String Created_By, Timestamp Last_Update,String Last_Updated_By)
    {
        try{
            // Get first level division id
            String sql = "select first_level_divisions.Division_ID from first_level_divisions\n" +
                        "where first_level_divisions.Division = \"" + firstLevel + "\";";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            // Execute query and get results
            ResultSet rs = ps.executeQuery();

            rs.next();
            int first_level_division_id = rs.getInt("Division_ID");

            // Insert customer into the database
            String sqlt = "insert into customers values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            // Prepared statement
            PreparedStatement pst = DBConnection.getConnection().prepareStatement(sqlt, Statement.RETURN_GENERATED_KEYS);
            // Add question marks
            pst.setString(1,fullName);
            pst.setString(2,address);
            pst.setString(3,postalCode);
            pst.setString(4,phoneNumber);
            pst.setTimestamp(5,Create_Date);
            pst.setString(6,Created_By);
            pst.setTimestamp(7,Last_Update);
            pst.setString(8,Last_Updated_By);
            pst.setInt(9, first_level_division_id);

            pst.execute();

            ResultSet resultSet = pst.getGeneratedKeys();
            resultSet.next();
            int customerID = resultSet.getInt(1);

            // Create new customer object and add it to the list
            Customer cust = new Customer(customerID,fullName,address,country,firstLevel,
                    postalCode,phoneNumber,Create_Date,Created_By,Last_Update,Last_Updated_By);
            addCustomer(cust);

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    /** This method updates a given customer from both the database and the observable list. */
    public static void updateCustomer(int custid, String fullName, String address, int countryid, int firstLevelid,
                                      String postalCode, String phoneNumber, Timestamp Last_Update,String Last_Updated_By)
    {
        try{
            // Update the customer in the database
            String update = "update customers set Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?,\n" +
                    "Division_ID = ? where Customer_ID = ?;";
            // Prepared statement
            PreparedStatement psu = DBConnection.getConnection().prepareStatement(update);
            // Add question marks
            psu.setString(1,fullName);
            psu.setString(2,address);
            psu.setString(3,postalCode);
            psu.setString(4,phoneNumber);
            psu.setTimestamp(5,Last_Update);
            psu.setString(6,Last_Updated_By);
            psu.setInt(7,firstLevelid);
            psu.setInt(8, custid);

            psu.execute();

            // Get the country that corresponds to the country id
            String country = null;
            for (Country country1 : Country.getAllCountries())
            {
                if (country1.getCountry_ID() == countryid)
                {
                    country = country1.getCountry();
                }
            }

            // Get the first level division that corresponds to the given id
            String firstLevel = null;
            for (FirstLevelDivisions firstLevelDivisions : FirstLevelDivisions.getFirstLevelD())
            {
                if (firstLevelDivisions.getDivision_ID() == firstLevelid)
                {
                    firstLevel = firstLevelDivisions.getDivision();
                }
            }

            // Update the customer in the observable list
            for (Customer customers : getAllCustomers())
            {
                if(custid == customers.getCustid())
                {
                    customers.setFullName(fullName);
                    customers.setAddress(address);
                    customers.setCountry(country);
                    customers.setFirstLevel(firstLevel);
                    customers.setPostalCode(postalCode);
                    customers.setPhoneNumber(phoneNumber);
                    customers.setLast_Update(Last_Update);
                    customers.setLast_Updated_By(Last_Updated_By);
                }
            }

        }
        catch (SQLException ex)
        {
            // Do nothing
            ex.printStackTrace();
        }

    }

    /** This method is to add a customer. */
    public static void addCustomer(Customer customer) { allCustomers.add(customer); }
    /** This method gets all the customers. */
    public static ObservableList<Customer> getAllCustomers() { return allCustomers; }

    /** This method gets all the customers stored in the database. */
    public static ObservableList <Customer> getCustomersFromDB()
    {
        ObservableList<Customer> cList = FXCollections.observableArrayList();
        try{
            // My sql statement
            String sql = """
                    select Customer_ID, Customer_Name, Address, Postal_Code, Phone, customers.Create_Date, customers.Created_By, customers.Last_Update, customers.Last_Updated_By, customers.Division_ID, first_level_divisions.Division, first_level_divisions.COUNTRY_ID, countries.Country
                    from customers, first_level_divisions, countries
                    where customers.Division_ID = first_level_divisions.Division_ID and first_level_divisions.COUNTRY_ID = countries.Country_ID
                    order by customers.Customer_ID;""";

            // Create a prepared statement
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            // Execute query and get results
            ResultSet rs = ps.executeQuery();

            // Work through the result set one row at a time
            while (rs.next())
            {
                int custid = rs.getInt("Customer_ID");
                String fullName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String country = rs.getString("Country");
                String firstLevel = rs.getString("Division");
                String postalCode = rs.getString("Postal_Code");
                String phoneNumber = rs.getString("Phone");
                Timestamp Create_Date = rs.getTimestamp("Create_Date");
                String Created_By = rs.getString("Created_By");
                Timestamp Last_Update = rs.getTimestamp("Last_Update");
                String Last_Updated_By = rs.getString("Last_Updated_By");

                Customer cust = new Customer(custid,fullName,address,country,firstLevel,postalCode,phoneNumber,Create_Date,Created_By,Last_Update,Last_Updated_By);
                cList.add(cust);

            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return cList;
    }
    /** This method sets the allCustomers list to what is found in the database. */
    public static void setAllCustomers(ObservableList<Customer> allCustomers) {
        Customer.allCustomers = allCustomers;
    }

    /** This method makes the customer object return the customer id if it is called for a combo box. */
    @Override
    public String toString()
    {
        return String.valueOf((custid));
    }

    /** Getters and Setters for the appointment attributes. */
    public int getCustid() {
        return custid;
    }

    public void setCustid(int custid) {
        this.custid = custid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirstLevel() {
        return firstLevel;
    }

    public void setFirstLevel(String firstLevel) {
        this.firstLevel = firstLevel;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
