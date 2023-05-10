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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static java.time.ZoneOffset.UTC;

/** This is the controller class for the modify appointment screen. */
public class ModifyAppointmentScreen implements Initializable
{

    /** Setting the fx id references for the schedule table*/
    @FXML
    private TableView<Appointment> ScheduleTV;
    @FXML
    private TableColumn<Appointment, Integer> AppIDTC;
    @FXML
    private TableColumn<Appointment, String> TypeTC;
    @FXML
    private TableColumn<Appointment, Timestamp> StartTC;
    @FXML
    private TableColumn<Appointment, Timestamp> EndTC;
    @FXML
    private TableColumn<Appointment, Integer> CustIDTC;

    /** Setting the fx id references for the customer table*/
    @FXML
    private TableView<Customer> CustomersTV;
    @FXML
    private TableColumn<Customer, Integer> SCustIDTC;
    @FXML
    private TableColumn<Customer, String> CustNameTC;
    @FXML
    private TableColumn<Customer, String> CustPhoneTC;

    /** Setting the fx id references for the choice boxes*/
    @FXML
    private ComboBox<Customer> CustIDCB;
    @FXML
    private ComboBox<Contact> ContactNCB;
    @FXML
    private ComboBox<FirstLevelDivisions> FirstLDCB;
    @FXML
    private ComboBox<Country> CountryCB;
    @FXML
    private ComboBox<Users> UserCB;
    @FXML
    private ComboBox StartTime;
    @FXML
    private ComboBox EndTime;

    /** Setting the fx id references for the buttons and the label. */
    @FXML
    private Button CancelB;
    @FXML
    private Button SaveB;
    @FXML
    private Label TimezoneL;

    /** Setting the fx id references for the text fields. */
    @FXML
    private TextField AppIDTF;
    @FXML
    private TextField CountryTF;
    @FXML
    private TextField FirstLevelDTF;
    @FXML
    private TextField AddressTF;
    @FXML
    private TextField DescriptionTF;
    @FXML
    private TextField TitleTF;
    @FXML
    private TextField TypeTF;

    /** Setting the fx id references for the date pickers. */
    @FXML
    private DatePicker StartDate;
    @FXML
    private DatePicker EndDate;

    /** The next few variables are for scene switching, time conversions and data passing. */
    Stage stage;
    Parent scene;
    private Appointment ap;
    TimeZone etcTZ = TimeZone.getTimeZone(ZoneId.of("America/New_York"));
    TimeZone tz = LoginScreen.getCurrentTZ();

    /** This method initializes the class and sets things up to carry the data it needs.
     * It also has a lambda expression. I chose to use a lambda expression there because
     * I only need the new selection long enough to set the first level division combo
     * box to reflect that choice. */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // Set data for the combo boxes
        CustIDCB.setItems(Customer.getAllCustomers());
        ContactNCB.setItems(Contact.getAllContacts());
        FirstLDCB.setItems(FirstLevelDivisions.getFirstLevelD());
        CountryCB.setItems(Country.getAllCountries());
        UserCB.setItems(Users.getAllUsers());

        ContactNCB.setPromptText("Choose a contact...");
        CustIDCB.setPromptText("Choose a customer...");
        UserCB.setPromptText("Choose a user...");
        CountryCB.setPromptText("Country...");
        FirstLDCB.setPromptText("Choose a country first...");
        StartTime.setPromptText("Times...");
        EndTime.setPromptText("Times...");

        // Set the time combo boxes
        LocalTime start = LocalTime.of(0, 0);
        LocalTime end = LocalTime.of(0, 0);
        LocalTime endl = LocalTime.of(23, 0);
        while (start.isBefore(endl.plusSeconds(1)))
        {
            StartTime.getItems().add(start);
            EndTime.getItems().add(end);
            start = start.plusMinutes(30);
            end = start.plusMinutes(30);
        }

        // Set first level division choice box to display the divisions for the chosen country
        CountryCB.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
        {
            if (newSelection != null)
            {
                FirstLDCB.setPromptText("Choose a division...");
                FirstLDCB.setItems(FirstLevelDivisions.getFirstLDByCountry(newSelection));
            }
        } );

        // Set data for the schedule table
        ScheduleTV.setItems(Appointment.getAllAppointments());

        AppIDTC.setCellValueFactory(new PropertyValueFactory<>("appid"));
        TypeTC.setCellValueFactory(new PropertyValueFactory<>("type"));
        StartTC.setCellValueFactory(new PropertyValueFactory<>("start"));
        EndTC.setCellValueFactory(new PropertyValueFactory<>("end"));
        CustIDTC.setCellValueFactory(new PropertyValueFactory<>("custid"));

        // Set data for the customers table
        CustomersTV.setItems(Customer.getAllCustomers());

        SCustIDTC.setCellValueFactory(new PropertyValueFactory<>("custid"));
        CustNameTC.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        CustPhoneTC.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        // Get data from the main screen
         ap = MainMenu.getmodifyApp();

        // Display appointment data
        AppIDTF.setText(String.valueOf(ap.getAppid()));
        TitleTF.setText(ap.getTitle());
        StartTime.getSelectionModel().select(ap.getStart().toLocalDateTime().toLocalTime());
        EndTime.getSelectionModel().select(ap.getEnd().toLocalDateTime().toLocalTime());
        StartDate.setValue(ap.getStart().toLocalDateTime().toLocalDate());
        EndDate.setValue(ap.getEnd().toLocalDateTime().toLocalDate());
        CountryCB.setValue(Country.searchCountryS(ap.getCountry()));
        FirstLDCB.setValue(FirstLevelDivisions.searchFirstLevel(ap.getFirstLevel()));
        CountryTF.setText(String.valueOf(CountryCB.getValue()));
        FirstLevelDTF.setText(String.valueOf(FirstLDCB.getValue()));
        AddressTF.setText(ap.getAddress());
        DescriptionTF.setText(ap.getDescription());
        TypeTF.setText(ap.getType());
        UserCB.setValue(Users.searchUsers(ap.getUser()));

        for (Customer cust : Customer.getAllCustomers())
        {
            if (ap.getCustid() == cust.getCustid())
            {
                CustIDCB.setValue(cust);
            }
        }
        for (Contact contact : Contact.getAllContacts())
        {
            if (ap.getContact().equals(contact.getName()))
            {
                ContactNCB.setValue(contact);
            }
        }

        // Set customer id combo box to whichever customer is chosen from the table
        CustomersTV.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
        {
            if (newSelection != null)
            {
                for (Customer cust : Customer.getAllCustomers())
                {
                    if (cust.getCustid() == newSelection.getCustid())
                    {
                        CustIDCB.setValue(cust);
                        break;
                    }
                }
            }
        } );

        // Set the country and first level text fields to whatever is selected from the combo box
        CountryCB.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
        {
            if (newSelection != null)
            {
                CountryTF.setText(newSelection.getCountry());
            }
        });
        FirstLDCB.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
        {
            if (newSelection != null)
            {
                FirstLevelDTF.setText(newSelection.getDivision());
            }
        });

        // Display the timezone
        displayTimezone();
    }

    /** This method saves the entered data by updating the appointment and returns to the main screen. */
    public void saveAppointment(MouseEvent mouseEvent) throws IOException
    {
        // Reset field styles in case they were changed by an error message
        resetFieldStyles();

        // Validate data
        if (!validateData())
        {
            return;
        }

        // Check if there is a country and/or first level
        String country = null;
        String firstlevel = null;
        if (CountryCB.getSelectionModel().getSelectedItem() == null)
        {
            if (confirmationWindows(1))
            {
                country = ap.getCountry();

            }
            else {return;}
        }
        if (CountryCB.getSelectionModel().getSelectedItem() != null)
        { country = String.valueOf(CountryCB.getSelectionModel().getSelectedItem().getCountry()); }

        if (FirstLDCB.getSelectionModel().getSelectedItem() == null)
        {
            if (confirmationWindows(2))
            {
                firstlevel = ap.getFirstLevel();
            }
            else {return;}
        }
        if (FirstLDCB.getSelectionModel().getSelectedItem() != null)
        { firstlevel = String.valueOf(FirstLDCB.getSelectionModel().getSelectedItem().getDivision()); }

        // Gather the data
        int appID = ap.getAppid();
        String title = TitleTF.getText().trim();
        String description = DescriptionTF.getText().trim();
        String type = TypeTF.getText().trim();
        String address = AddressTF.getText().trim();
        LocalDateTime start = LocalDateTime.of(StartDate.getValue(),(LocalTime) StartTime.getSelectionModel().getSelectedItem());
        LocalDateTime end = LocalDateTime.of(EndDate.getValue(),(LocalTime) EndTime.getSelectionModel().getSelectedItem());
        ZonedDateTime startZ = start.atZone(tz.toZoneId());
        ZonedDateTime endZ = end.atZone(tz.toZoneId());
        ZonedDateTime lastupdate = ZonedDateTime.now();
        Timestamp Last_Update = Timestamp.valueOf(lastupdate.toLocalDateTime());
        String Last_Updated_By = LoginScreen.getCUser().getUser_Name();
        int custid = Integer.parseInt(String.valueOf(CustIDCB.getSelectionModel().getSelectedItem().getCustid()));
        String user = LoginScreen.getCUser().getUser_Name();
        String contact = String.valueOf(ContactNCB.getSelectionModel().getSelectedItem().getName());

        // Update appointment
        Appointment.updateAppointment(appID,title,description,type,country,firstlevel,address,startZ,endZ,Last_Update,Last_Updated_By,custid,user,contact);

        // Return to main menu
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method resets the field styles back to normal in case they were changed by the error messages.*/
    private void resetFieldStyles()
    {
        TypeTF.setStyle("-fx-border-color: light gray");
        AddressTF.setStyle("-fx-border-color: light gray");
        DescriptionTF.setStyle("-fx-border-color: light gray");
    }

    /** This method contains all the checks to ensure all the data is correctly entered.*/
    private boolean validateData()
    {
        // Make sure nothing is empty
        if (TitleTF.getText() == null)
        {
            MainMenu.errorAppointment(1, TitleTF);
            return false;
        }
        if (TypeTF.getText() == null)
        {
            MainMenu.errorAppointment(1,TypeTF);
            return false;
        }
        if (StartDate.getValue() == null)
        {
            MainMenu.errorAppointment(5,null);
            return false;
        }
        if (EndDate.getValue() == null)
        {
            MainMenu.errorAppointment(6,null);
            return false;
        }
        if (AddressTF.getText() == null)
        {
            MainMenu.errorAppointment(1,AddressTF);
            return false;
        }
        if (DescriptionTF.getText().isEmpty())
        {
            MainMenu.errorAppointment(1,DescriptionTF);
            return false;
        }
        if (StartTime.getSelectionModel().getSelectedItem() == null)
        {
            MainMenu.errorAppointment(5, null);
            return false;
        }
        if (EndTime.getSelectionModel().getSelectedItem() == null)
        {
            MainMenu.errorAppointment(6, null);
            return false;
        }
        if (CustIDCB.getSelectionModel().getSelectedItem() == null)
        {
            MainMenu.errorAppointment(2, null);
            return false;
        }
        if (ContactNCB.getSelectionModel().getSelectedItem() == null)
        {
            MainMenu.errorAppointment(3, null);
            return false;
        }
        // Gather the time data
        LocalTime st = (LocalTime) StartTime.getSelectionModel().getSelectedItem();
        LocalTime et = (LocalTime) EndTime.getSelectionModel().getSelectedItem();
        LocalTime sBHoursEtc = LocalTime.of(8, 0);
        LocalTime eBHoursEtc = LocalTime.of(22, 0);
        LocalDateTime start = LocalDateTime.of(StartDate.getValue(),st);
        LocalDateTime end = LocalDateTime.of(EndDate.getValue(),et);
        LocalDateTime estbizs = LocalDateTime.of(StartDate.getValue(), sBHoursEtc);
        LocalDateTime estbize = LocalDateTime.of(EndDate.getValue(), eBHoursEtc);
        ZonedDateTime estzdt = estbizs.atZone(etcTZ.toZoneId());
        ZonedDateTime estzd = estbize.atZone(etcTZ.toZoneId());
        ZonedDateTime sBHours = estzdt.withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime eBHours = estzd.withZoneSameInstant(ZoneId.systemDefault());
        int custid = Integer.parseInt(String.valueOf(CustIDCB.getSelectionModel().getSelectedItem().getCustid()));

        // Check for appropriate times
        if (start.isAfter(end))
        {
            MainMenu.errorAppointment(7, null);
            return false;
        }
        // Check that the hours are within business hours
        if (start.isBefore(sBHours.toLocalDateTime()) || end.isAfter(eBHours.toLocalDateTime()))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding the appointment");
            alert.setHeaderText("This appointment is not within business hours.");
            alert.setContentText("Please change the times to be between " + sBHours.toLocalTime() + " and " + eBHours.toLocalTime() + ".");
            alert.showAndWait();
            return false;
        }
        // Check overlapping appointments for customers
        Customer cust = null;
        for (Customer customer : Customer.getAllCustomers())
        {
            if (customer.getCustid() == custid)
            {
                Appointment.addAppointmentsByCustomer(customer);
                cust = customer;
            }
        }
        return overLappingApps(cust);
    }

    /** This method checks to see if the given customer has another appointment that would overlap. */
    public boolean overLappingApps(Customer customer)
    {
        LocalDateTime start = LocalDateTime.of(StartDate.getValue(),(LocalTime) StartTime.getSelectionModel().getSelectedItem());
        LocalDateTime end = LocalDateTime.of(EndDate.getValue(), (LocalTime) EndTime.getSelectionModel().getSelectedItem());
        Appointment.addAppointmentsByCustomer(customer);
        for (Appointment appcons : Appointment.getAppointmentsByCustomer())
        {
            LocalDateTime startc = appcons.getStart().toLocalDateTime();
            LocalDateTime endc = appcons.getEnd().toLocalDateTime();

            if ((start.toLocalDate().isEqual(startc.toLocalDate()) || end.toLocalDate().isEqual(endc.toLocalDate())))
            {
                if (start.isBefore(startc) || start.isEqual(startc))
                {
                    if (end.isAfter(startc))
                    {
                        return overlappingAlert(customer, appcons);
                    }
                }
                if (start.isAfter(startc))
                {
                    if (start.isBefore(endc))
                    {
                        return overlappingAlert(customer, appcons);
                    }
                }
            }
        }
        return true;
    }

    /** This method provides the alert if a given customer has an overlapping appointment. */
    private Boolean overlappingAlert(Customer customer, Appointment appointment)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error adding the appointment");
        alert.setHeaderText("Please change the times.");
        alert.setContentText("This appointment overlaps with appointment [" + appointment.getAppid() + "] \n for " + customer.getFullName() + ".");
        alert.showAndWait();
        return false;
    }

    /** This method sets timezone label to reflect the system timezone. */
    public void displayTimezone ()
    {
        TimezoneL.setText(LoginScreen.getCurrentTZ().getDisplayName());
    }

    /** This method provides the confirmation windows for the screen. */
    private boolean confirmationWindows (int code)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save the appointment");

        if (code == 1)
        {
            alert.setContentText("Are you sure you want to save the appointment without a country?");
        }
        if (code == 2)
        {
            alert.setContentText("Are you sure you want to save the appointment without a first level division?");
        }
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /** This method returns to the main menu. */
    public void cancel(MouseEvent mouseEvent) throws IOException
    {
        // Load the main screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}