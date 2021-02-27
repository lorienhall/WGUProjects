package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/** This is the controller class for the main menu screen. */
public class MainMenu implements Initializable
{
    /** Setting the fx id references for the customers table. */
    @FXML
    private TableView<Customer> CustomersTV;
    @FXML
    private TableColumn<Customer, Integer> CustIDTCcust;
    @FXML
    private TableColumn<Customer, String> CustNameTC;
    @FXML
    private TableColumn<Customer, String> CustPhoneTC;
    @FXML
    private TableColumn<Customer, String> CountryTC;
    @FXML
    private TableColumn<Customer, String> FirstLevelTC;

    /** Setting the fx id references for the appointments table. */
    @FXML
    private TableView<Appointment> ScheduleTV;
    @FXML
    private TableColumn<Appointment, Integer> AppIDTC;
    @FXML
    private TableColumn<Appointment, String> TitleTC;
    @FXML
    private TableColumn<Appointment, String> TypeTC;
    @FXML
    private TableColumn<Appointment, Timestamp> StartTC;
    @FXML
    private TableColumn<Appointment, Timestamp> EndTC;
    @FXML
    private TableColumn<Appointment, Integer> CustIDTCapp;

    /** The next few variables are for scene switching and data passing. */
    Stage stage;
    Parent scene;
    private static Appointment modifyApp = null;
    public static Appointment getmodifyApp() { return modifyApp; }
    private static Customer modifyCust = null;
    public static Customer getmodifyCust() { return modifyCust; }

    /** This method initializes the class and sets things up to carry the data it needs. */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // Set data for the customers table
        CustomersTV.setItems(Customer.getAllCustomers());

        CustIDTCcust.setCellValueFactory(new PropertyValueFactory<>("custid"));
        CustNameTC.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        CustPhoneTC.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        CountryTC.setCellValueFactory(new PropertyValueFactory<>("country"));
        FirstLevelTC.setCellValueFactory(new PropertyValueFactory<>("firstLevel"));

        // Set data for the schedule table
        ScheduleTV.setItems(Appointment.getAllAppointments());

        AppIDTC.setCellValueFactory(new PropertyValueFactory<>("appid"));
        TitleTC.setCellValueFactory(new PropertyValueFactory<>("title"));
        TypeTC.setCellValueFactory(new PropertyValueFactory<>("type"));
        StartTC.setCellValueFactory(new PropertyValueFactory<>("start"));
        EndTC.setCellValueFactory(new PropertyValueFactory<>("end"));
        CustIDTCapp.setCellValueFactory(new PropertyValueFactory<>("custid"));

        // Alert if there is an appointment within 15 mins away
        if (LoginScreen.user != null)
        {
            appointmentAlert();
            LoginScreen.user = null;
        }

    }

    /** This method alerts the user if there are any appointments within the next 15 minutes. */
    private void appointmentAlert()
    {
        // Check if there are appointments
        int numapp = 0;
        Appointment app = null;
        for (Appointment appointment : Appointment.getAllAppointments())
        {
            LocalDateTime start = appointment.getStart().toLocalDateTime();
            String user = LoginScreen.getCUser().getUser_Name().trim();

            if (start.isAfter(LocalDateTime.now()) && appointment.getUser().equals(user))
            {
                if (start.minusMinutes(30).isBefore(LocalDateTime.now()))
                {
                    numapp++;
                    if (app == null) { app = appointment; }
                }

            }
        }

        // Provide alerts
        appointmentAlerts(app, numapp);
        return;
    }

    /** This method provides the alerts for the appointmentAlert method. */
    public static boolean appointmentAlerts (Appointment app, int NumApp)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (NumApp != 0)
        {
            alert.setTitle("Alert!");
            alert.setHeaderText("You have " + NumApp + " appointment(s) in the next fifteen minutes");
            alert.setContentText("Appointment Title: " + app.getTitle() + "\nAppointment ID: " + app.getAppid() + "\nStart: " + app.getStart() + "\nEnd: " + app.getEnd());
            Optional<ButtonType> result = alert.showAndWait();
            return result.get() == ButtonType.OK;
        }
        else
        {
            alert.setTitle("Alert!");
            alert.setHeaderText("You have " + NumApp + " appointment(s) in the next fifteen minutes");
            Optional<ButtonType> result = alert.showAndWait();
            return result.get() == ButtonType.OK;
        }
    }

    /** This method loads the add customer screen. */
    public void addCustomer(javafx.scene.input.MouseEvent mouseEvent) throws IOException
    {
        // Load the add customer screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddCustomerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method loads the modify customer screen. */
    public void modifyCustomer(MouseEvent mouseEvent) throws IOException
    {
        // Set up for data passing
        Customer cu = CustomersTV.getSelectionModel().getSelectedItem();
        modifyCust = cu;

        // Validate something is selected
        if (CustomersTV.getSelectionModel().getSelectedItem() == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Nothing Selected!");
            alert.setContentText("Please select a customer to modify,");
            alert.showAndWait();
        }
        else
        {
            // Load the modify customer screen
            stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/ModifyCustomerScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /** This method deletes the selected customer. */
    public void deleteCustomer(MouseEvent mouseEvent)
    {
        // Validate something is selected
        if (CustomersTV.getSelectionModel().getSelectedItem() == null)
        {
            errorCustomer(3,null);
            return;
        }
        Customer cust = CustomersTV.getSelectionModel().getSelectedItem();

        // Check the customer has no appointments
        for (Appointment appointment : Appointment.getAllAppointments())
        {
            if (cust.getCustid() == appointment.getCustid())
            {
                errorCustomer(4,null);
                return;
            }
        }

        // Display confirmation message
        if (!confirmationWindows(1,null, cust)) { return; }

        // Delete customer
        Customer.deleteCustomer(cust);
    }

    /** This method loads the add appointment screen. */
    public void addApp(MouseEvent mouseEvent) throws IOException
    {
        // Load the add appointment screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddAppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method loads the modify appointment screen. */
    public void modifyApp(MouseEvent mouseEvent) throws IOException
    {
        // Validate something is selected
        if (ScheduleTV.getSelectionModel().getSelectedItem() == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Nothing Selected!");
            alert.setContentText("Please select an appointment to modify,");
            alert.showAndWait();
        }

        // Set up for data passing
        Appointment ap = ScheduleTV.getSelectionModel().getSelectedItem();
        modifyApp = ap;

        // Load the modify appointment screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ModifyAppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method deletes the selected appointment. */
    public void deleteApp(MouseEvent mouseEvent)
    {
        // Validate something is selected
        if (ScheduleTV.getSelectionModel().getSelectedItem() == null)
        {
            errorAppointment(8,null);
            return;
        }
        Appointment app = ScheduleTV.getSelectionModel().getSelectedItem();

        // Display confirmation message
       if (!confirmationWindows(1, app, null)) { return; }

        // Delete appointment
        Appointment.deleteAppointment(app);
    }

    /** This method loads the calender screen. */
    public void viewSchedule(MouseEvent mouseEvent) throws IOException
    {
        // Load the schedule screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/CalenderScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method loads the customers screen. */
    public void viewCustomers(MouseEvent mouseEvent) throws IOException
    {
        // Load the customers screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/CustomersScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method loads the reports screen. */
    public void getReports(MouseEvent mouseEvent) throws IOException
    {
        // Load the reports screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ReportsScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method provides the confirmation popup for the class. */
    public static boolean confirmationWindows (int code, Appointment app, Customer cust)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        if (app != null)
        {
            alert.setTitle("Delete " + app.getTitle() + ".");
            if (code == 1)
            {
                alert.setContentText("Are you sure you want to delete appointment " + app.getAppid() + ": " + app.getType() + "?");
            }
        }
        if (cust != null)
        {
            alert.setTitle("Delete " + cust.getFullName() + ".");
            if (code == 1)
            {
                alert.setContentText("Are you sure you want to delete customer " + cust.getCustid() + ": " + cust.getFullName() + "?");
            }
        }

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /** This method provides the error messages for the appointments. */
    public static void errorAppointment(int code, TextField field)
    {
        fieldError(field);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error adding the appointment");
        alert.setHeaderText("Cannot add the appointment");

        switch (code) {
            case 1 -> alert.setContentText("A field is empty!");
            case 2 -> alert.setContentText("Please choose a customer.");
            case 3 -> alert.setContentText("Please choose a contact.");
            case 4 -> alert.setContentText("Invalid format!");
            case 5 -> alert.setContentText("You must pick a start date AND a start time.");
            case 6 -> alert.setContentText("You must pick an end date AND an end time.");
            case 7 -> alert.setContentText("The end of an appointment cannot be before the beginning.");
            case 8 -> {
                alert.setTitle("Cannot delete an appointment");
                alert.setHeaderText("There is no appointment selected!");
                alert.setContentText("Please choose an appointment to delete.");
            }
            case 9 -> alert.setContentText("This appointment overlaps with another appointment for the chosen customer!");
            default -> alert.setContentText("Unknown error!");
        }
        alert.showAndWait();
    }

    /** This method provides the error messages for the customers. */
    public static void errorCustomer(int code, TextField field)
    {
        fieldError(field);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error adding the customer");
        alert.setHeaderText("Cannot add the customer");

        switch (code) {
            case 1 -> alert.setContentText("A field is empty!");
            case 2 -> alert.setContentText("Invalid format!");
            case 3 -> {
                alert.setTitle("Cannot delete a customer");
                alert.setHeaderText("There is no customer selected!");
                alert.setContentText("Please choose an customer to delete.");
            }
            case 4 -> {
                alert.setTitle("Cannot delete a customer");
                alert.setHeaderText("The selected customer has appointments!");
                alert.setContentText("Please delete all the customers appointments before deleting the customer.");
            }
            case 5 -> {
                alert.setHeaderText("Invalid Phone Number!");
                alert.setContentText("Please enter a valid phone number.");
            }
            default -> alert.setContentText("Unknown error!");
        }
        alert.showAndWait();
    }

    /** This method changes the field color to red for the error messages. */
    public static void fieldError(TextField field)
    {
        if (field != null){
            field.setStyle("-fx-border-color: red");
        }
    }
}

