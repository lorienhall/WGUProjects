package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** This class sets up the connection to the database. */
public class DBConnection
{
    
    /** JDBC URL parts */
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com/WJ0819V";

    /** JDBC URL */
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    private static final String username = "U0819V";
    private static final String password = "53689193076";

    /** Driver and connection interface reference */
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;

    /** This method opens the databases connection*/
    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection Successful!");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /** This method gets the database connection. */
    public static Connection getConnection() {return conn;}

    /** This method closes the database connection. */
    public static void closeConnection(Connection conn)
    {
        try
        {
            conn.close();
            System.out.println("Connection Closed.");
        }
        catch (SQLException e)
        {
            System.out.println("Error: "+ e.getMessage());
        }
    }



}
