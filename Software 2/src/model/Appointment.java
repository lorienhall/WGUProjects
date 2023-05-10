package model;

import controller.LoginScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;

import java.sql.*;
import java.time.*;
import java.util.TimeZone;

import static java.time.ZoneOffset.UTC;

/** This is the class for the appointments. */
public class Appointment
{
    /** TimeZone stuff. */
    static TimeZone tz = LoginScreen.getCurrentTZ();

    /** Attribute declarations. */
    private static ObservableList <Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList <Appointment> appointmentByCustomer = FXCollections.observableArrayList();
    private static ObservableList <Appointment> appointmentByContact = FXCollections.observableArrayList();
    private int appid;
    private String title;
    private String description;
    private String type;
    private String country;
    private String firstLevel;
    private String address;
    private String location;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private Timestamp Create_Date;
    private String Created_By;
    private Timestamp Last_Update;
    private String Last_Updated_By;
    private int custid;
    private String user;
    private String contact;

    /** Appointments constructor. */
    public Appointment(int appid, String title, String description, String type, String country,
                       String firstlevel, String address, ZonedDateTime start, ZonedDateTime end, Timestamp Create_Date,
                       String Created_By, Timestamp Last_Update,String Last_Updated_By, int custid,
                       String user, String contact)
    {
        this.appid = appid;
        this.title = title;
        this.description = description;
        this.type = type;
        this.country = country;
        this.firstLevel = firstlevel;
        this.address = address;
        this.location = address + ", " + firstlevel + ", " + country;
        this.start = start;
        this.end = end;
        this.Create_Date = Create_Date;
        this.Created_By = Created_By;
        this.Last_Update = Last_Update;
        this.Last_Updated_By = Last_Updated_By;
        this.custid = custid;
        this.user = user;
        this.contact = contact;
    }

    /** This method deletes a selected appointment from both the database and the observable list. */
    public static void deleteAppointment (Appointment appointment)
    {
        try {
            int appID = appointment.getAppid();
            // Delete appointment from the database
            String delete = "delete from appointments where Appointment_ID = " + appID + ";";
            PreparedStatement psd = DBConnection.getConnection().prepareStatement(delete);
            psd.execute();

            // Delete the appointment from the observable list
            allAppointments.remove(appointment);
        }
        catch (SQLException e)
        {
            // Do nothing
        }
    }

    /** This method creates a new appointment. */
    public static void newAppointment(String title, String description, String type, String country,
                                      String firstlevel, String address, ZonedDateTime start, ZonedDateTime end, Timestamp Create_Date,
                                      String Created_By, Timestamp Last_Update,String Last_Updated_By, int custid,
                                      String user, String contact)
    {
        try{
            // Set location
            String location = address + "," + firstlevel + "," + country;

            // Get first level division id
            String sql = "select first_level_divisions.Division_ID from first_level_divisions\n" +
                    "where first_level_divisions.Division = \"" + firstlevel + "\";";
            // Create a prepared statement
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            // Execute query and get results
            ResultSet rs = ps.executeQuery();

            // Work through the result set one row at a time
            rs.next();
            int first_level_division_id = rs.getInt("Division_ID");

            // Get user id
            String uid = "select users.User_ID from users,appointments where users.User_ID = appointments.User_ID and users.User_Name = \""
                    + user + "\"";

            PreparedStatement psu = DBConnection.getConnection().prepareStatement(uid);
            ResultSet rsu = psu.executeQuery();

            rsu.next();
            int userID = rsu.getInt("User_ID");

            // Get contact id
            String cid = "select Contact_ID from contacts where contacts.Contact_Name = \""
                    + contact + "\"";

            PreparedStatement psc = DBConnection.getConnection().prepareStatement(cid);
            ResultSet rsc = psc.executeQuery();

            rsc.next();
            int contactID = rsc.getInt("Contact_ID");

            // Time zone conversions
            Timestamp st = Timestamp.valueOf(start.toLocalDateTime());
            Timestamp et = Timestamp.valueOf(end.toLocalDateTime());

            // Insert appointment into the database
            String insert = "insert into appointments values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            // Prepared statement
            PreparedStatement pst = DBConnection.getConnection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            // Add question marks
            pst.setString(1,title);
            pst.setString(2,description);
            pst.setString(3,location);
            pst.setString(4,type);
            pst.setTimestamp(5,st);
            pst.setTimestamp(6,et);
            pst.setTimestamp(7,Create_Date);
            pst.setString(8,Created_By);
            pst.setTimestamp(9, Last_Update);
            pst.setString(10,Last_Updated_By);
            pst.setInt(11,custid);
            pst.setInt(12,userID);
            pst.setInt(13,contactID);

            pst.execute();

            ResultSet resultSet = pst.getGeneratedKeys();
            resultSet.next();
            int appointmentID = resultSet.getInt(1);

            // Create new appointment object and add it to the list
            Appointment app = new Appointment(appointmentID, title, description, type, country, firstlevel , address, start, end, Create_Date,
                    Created_By, Last_Update, Last_Updated_By, custid, user, contact);
            addAppointment(app);

        }
        catch (SQLException ex)
        {
            // Ignore
        }
    }

    /** This method modifies an appointment is both the database and the observable list. */
    public static void updateAppointment(int appid, String title, String description, String type, String country,
                                        String firstlevel, String address, ZonedDateTime start, ZonedDateTime end,
                                        Timestamp Last_Update,String Last_Updated_By, int custid, String user, String contact)
    {
        try{
            // Set location
            String location = address + "," + firstlevel + "," + country;

            // Get user id
            String uid = "select users.User_ID from users\n" +
                    "where users.User_Name = \"" + user + "\";";

            PreparedStatement psu = DBConnection.getConnection().prepareStatement(uid);
            ResultSet rsu = psu.executeQuery();

            rsu.next();
            int userID = rsu.getInt("User_ID");

            // Get contact id
            int contactID = Contact.searchContacts(contact).getConid();

            // Time zone conversions
            Timestamp st = Timestamp.valueOf(start.toLocalDateTime());
            Timestamp et = Timestamp.valueOf(end.toLocalDateTime());

            // Update the appointment in the database
            String update = "update appointments set Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, " +
                    "Customer_ID = ?, User_ID = ?, Contact_ID = ? where Appointment_ID = ?;";
            // Prepared statement
            PreparedStatement pst = DBConnection.getConnection().prepareStatement(update);
            // Add question marks
            pst.setString(1,title);
            pst.setString(2,description);
            pst.setString(3,location);
            pst.setString(4,type);
            pst.setTimestamp(5,st);
            pst.setTimestamp(6,et);
            pst.setTimestamp(7,Last_Update);
            pst.setString(8,Last_Updated_By);
            pst.setInt(9,custid);
            pst.setInt(10,userID);
            pst.setInt(11,contactID);
            pst.setInt(12,appid);

            pst.execute();

            // Update the appointment in the observable list
            for (Appointment appointment : getAllAppointments())
            {
                if(appid == appointment.getAppid())
                {
                    appointment.setTitle(title);
                    appointment.setDescription(description);
                    appointment.setCountry(country);
                    appointment.setFirstLevel(firstlevel);
                    appointment.setAddress(address);
                    appointment.setType(type);
                    appointment.setStart(start);
                    appointment.setEnd(end);
                    appointment.setLast_Update(Last_Update);
                    appointment.setLast_Updated_By(Last_Updated_By);
                    appointment.setCustid(custid);
                    appointment.setUser(user);
                    appointment.setContact(contact);
                }
            }
        }
        catch (SQLException ex)
        {
            // Ignore
        }

    }

    /** This method gets all the appointments stored in the database. */
    public static ObservableList <Appointment> getAppointmentsFromDB()
    {
        ObservableList<Appointment> cList = FXCollections.observableArrayList();
        try{
            // Sql statement to get the appointments
            String sql = "select * from appointments";
            // Create a prepared statement
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            // Execute query and get results
            ResultSet rs = ps.executeQuery();

            // Work through the result set one row at a time
            while (rs.next())
            {
                int appid = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                Timestamp Create_Date = rs.getTimestamp("Create_Date");
                String Created_By = rs.getString("Created_By");
                Timestamp Last_Update = rs.getTimestamp("Last_Update");
                String Last_Updated_By = rs.getString("Last_Updated_By");
                int custid = rs.getInt("Customer_ID");
                int userid = rs.getInt("User_ID");
                int contactid = rs.getInt("Contact_ID");

                String[] l = location.split(",");
                String country;
                String firstLevel;
                String address;
                if (l.length != 1)
                {
                    country = l[2];
                    firstLevel = l[1];
                    address = l[0];
                }
                else
                {
                    country = "";
                    firstLevel = "";
                    address = l[0];
                }



                // Create zoned date times
                ZonedDateTime st = start.atZone(tz.toZoneId());
                ZonedDateTime et = end.atZone(tz.toZoneId());

                // Get user name
                String uid = "select users.User_Name from users where users.User_ID = \"" + userid + "\"";

                PreparedStatement psu = DBConnection.getConnection().prepareStatement(uid);
                ResultSet rsu = psu.executeQuery();

                rsu.next();
                String user = rsu.getString("User_Name");

                // Get contact id
                String cid = "select contacts.Contact_Name from contacts where contacts.Contact_ID = \"" + contactid + "\"";

                PreparedStatement psc = DBConnection.getConnection().prepareStatement(cid);
                ResultSet rsc = psc.executeQuery();

                rsc.next();
                String contact = rsc.getString("Contact_Name");

                Appointment app = new Appointment(appid, title, description, type, country, firstLevel, address , st, et, Create_Date,
                        Created_By, Last_Update, Last_Updated_By, custid, user, contact);
                cList.add(app);
            }

        }
        catch (SQLException ex)
        {
            // Ignore
        }
        return cList;
    }
    /** This method adds an appointment to the observable list. */
    public static void addAppointment(Appointment appointment) { allAppointments.add(appointment); }
    /** This method gets all the appointments from the observable list. */
    public static ObservableList <Appointment> getAllAppointments() { return allAppointments;}

    /** This method gets the appointments by a customer. */
    public static ObservableList <Appointment> getAppointmentsByCustomer() { return appointmentByCustomer; }
    /** This method adds an appointment for a customer to the observable list. */
    public static void addAppointmentsByCustomer(Customer customer)
    {
        appointmentByCustomer.clear();
        for (Appointment appointment : allAppointments)
        {
            if (appointment.getCustid() == customer.getCustid()) {
                appointmentByCustomer.add(appointment);
            }
        }
    }

    /** This method gets the appointments by a contact. */
    public static ObservableList <Appointment> getAppointmentsByContact() { return appointmentByContact; }
    /** This method adds an appointment for a contact to the observable list. */
    public static void addAppointmentsByContact(Contact contact)
    {
        appointmentByContact.clear();
        for (Appointment appointment : allAppointments)
        {
            if (appointment.getContact().equals(contact.getName())) {
                appointmentByContact.add(appointment);
            }
        }
    }

    /** This method sets the observable list to the provided list of appointments. */
    public static void setAllAppointments(ObservableList<Appointment> allAppointments) {
        Appointment.allAppointments = allAppointments;
    }

    /** This method makes the appointment object return the customer id if called for a combo box. */
    @Override
    public String toString()
    {
        return (Integer.toString(custid));
    }

    /** Getters and Setters for the appointment attributes. */
    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public int getCustid() {
        return custid;
    }

    public void setCustid(int custid) {
        this.custid = custid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
