package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import utils.DBConnection;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

/** This is the main class. It loads the login screen and establishes the database connection. */
public class Main extends Application {

    /** This method loads the main screen and add the objects to their observable lists. */
    @Override
    public void start(Stage primaryStage) throws Exception{
        // Set up the observable lists
        Customer.setAllCustomers(Customer.getCustomersFromDB());
        Appointment.setAllAppointments(Appointment.getAppointmentsFromDB());
        Contact.setAllContacts(Contact.getContactsFromDB());
        FirstLevelDivisions.setAllDivisions(FirstLevelDivisions.getFirstLevelDFromDB());
        Country.setAllCountries(Country.getCountriesFromDB());
        Users.setAllUsers(Users.getUsersFromDB());

        // Load the login screen
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        primaryStage.setTitle("MySQL Database Scheduler");
        primaryStage.setScene(new Scene(root, 369, 300));
        primaryStage.show();

    }

    /** This is the main method. It open and closes the database connection. */
    public static void main(String[] args) throws SQLException
    {
        Connection conn = DBConnection.startConnection();

        launch(args);

        DBConnection.closeConnection(conn);
    }


}
