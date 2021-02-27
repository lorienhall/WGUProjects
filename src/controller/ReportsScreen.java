package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import model.Contact;
import model.Customer;
import model.Types;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/** This is the controller class for the reports screen. */
public class ReportsScreen  implements Initializable
{
    /** Setting the fx id references for the schedule reports table. */
    @FXML
    private TableView<Appointment> ScheduleReportsTV;
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
    @FXML
    private TableColumn<Appointment, String> DescriptionTC;

    /** Setting the fx id references for the choice boxes. */
    @FXML
    private ComboBox<Contact> ContactPickCB;
    @FXML
    private ComboBox<String> MonthPickCB1;
    @FXML
    private ComboBox<String> MonthPickCB2;

    /** Setting the fx id references for misc items. */
    @FXML
    private Button ExitB;
    @FXML
    private Label NumNewCustL;
    @FXML
    private Label TimezoneL;

    /** Setting the fx id references for the appointment reports table. */
    @FXML
    private TableView<Types> AppReportTV;
    @FXML
    private TableColumn<Types, String> Type;
    @FXML
    private TableColumn<Types, Integer> Num;

    /** The next two variables are for scene switching. */
    Stage stage;
    Parent scene;

    /** This method initializes the class and sets things up to carry the data it needs.
     * It also has three lambda expression. I chose to use a lambda expressions because
     * I only need the new selection long enough to set the tableview or label to
     * reflect that choice.*/
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // Set data for the combo boxes
        addMonth();
        ContactPickCB.setItems(Contact.getAllContacts());
        MonthPickCB1.setItems(months);
        MonthPickCB2.setItems(months);

        ContactPickCB.getSelectionModel().selectFirst();
        MonthPickCB1.getSelectionModel().selectFirst();
        MonthPickCB2.getSelectionModel().selectFirst();


        numNewCust(MonthPickCB2.getValue());
        addTypes();

        // Set data for the schedule table
        setContactScheduleTV(ContactPickCB.getValue());
        ScheduleReportsTV.setItems(Appointment.getAppointmentsByContact());

        AppIDTC.setCellValueFactory(new PropertyValueFactory<>("appid"));
        TitleTC.setCellValueFactory(new PropertyValueFactory<>("title"));
        TypeTC.setCellValueFactory(new PropertyValueFactory<>("type"));
        StartTC.setCellValueFactory(new PropertyValueFactory<>("start"));
        EndTC.setCellValueFactory(new PropertyValueFactory<>("end"));
        CustIDTCapp.setCellValueFactory(new PropertyValueFactory<>("custid"));
        DescriptionTC.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Set data for the appointment type report table
        setScheduleReportsTV(MonthPickCB1.getValue());
        AppReportTV.setItems(Types.getTypesByMonth());

        Type.setCellValueFactory(new PropertyValueFactory<>("types"));
        Num.setCellValueFactory(new PropertyValueFactory<>("numtypes"));

        // Change the schedule reports tableview if a new month is selected
        MonthPickCB1.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
        {
            if (newSelection != null)
            {
                setScheduleReportsTV(newSelection);
                AppReportTV.setItems(Types.getTypesByMonth());
            }
        });

        // Change the contact schedule tableview if a new contact is selected
        ContactPickCB.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
        {
            if (newSelection != null)
            {
                setContactScheduleTV(newSelection);
                ScheduleReportsTV.setItems(Appointment.getAppointmentsByContact());
            }
        });

        // Change the number of new customers label when a new month is chosen
        MonthPickCB2.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
        {
            if (newSelection != null)
            {
                NumNewCustL.setText(String.valueOf(numNewCust(newSelection)));
            }
        });

        // Display the timezone
        displayTimezone();
    }

    /** These variables are used to set the items for the two combo boxes that display months. */
    public static ObservableList<String> months = FXCollections.observableArrayList();
    private static final String[] m = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    /** This method adds all the months to the observable list. */
    private static void addMonth ()
    {
        for (String s : m) {
            months.add(s);
        }
    }

    /** This method and observable list are for displaying the number of each type of appointment by month. */
    private static final ObservableList<String> types = FXCollections.observableArrayList();
    private static void addTypes ()
    {
        types.clear();
        for (Appointment appointment : Appointment.getAllAppointments())
        {
            types.add(appointment.getType());
        }
    }

    /** This method returns to the main menu. */
    public void toMainMenu(MouseEvent mouseEvent) throws IOException
    {
        // Load the main screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** These two variables are determining the number of customers created in a month. */
    int monthValue = 0;
    int numNew = 0;
    /** This method gets the number of new customers in a given month. */
    private int numNewCust(String month)
    {
        // Get the month
        for (int i = 0; i < m.length; i++)
        {
            if (month.equals(m[i]))
            {
                monthValue = i;
            }
        }

        // Get the number of customers created in that month
        numNew = 0;
        for (Customer customer : Customer.getAllCustomers())
        {
           LocalDateTime cDate = customer.getCreate_Date().toLocalDateTime();
            if (cDate.getYear() == 2020 && cDate.getMonthValue() == monthValue+1)
            {
                numNew++;
            }
        }

        // Set the label to the num of new customers that month
        NumNewCustL.setText(String.valueOf(numNew));
        return numNew;
    }

    /** This method gets the equivalent month value based on the chosen month. */
    public void getMonth (String month)
    {
        // Get the month
        monthValue = 0;
        if (month.trim().equals("January")) { monthValue = 1; }
        if (month.trim().equals("February")) { monthValue = 2; }
        if (month.trim().equals("March")) { monthValue = 3; }
        if (month.trim().equals("April")) { monthValue = 4; }
        if (month.trim().equals("May")) { monthValue = 5; }
        if (month.trim().equals("June")) { monthValue = 6; }
        if (month.trim().equals("July")) { monthValue = 7; }
        if (month.trim().equals("August")) { monthValue = 8; }
        if (month.trim().equals("September")) { monthValue = 9; }
        if (month.trim().equals("October")) { monthValue = 10; }
        if (month.trim().equals("November")) { monthValue = 11; }
        if (month.trim().equals("December")) { monthValue = 12; }
    }

    /** This method sets timezone label to reflect the system timezone. */
    public void displayTimezone ()
    {
        TimezoneL.setText(LoginScreen.getCurrentTZ().getDisplayName());
    }

    /** This method sets the schedule tableview to reflect number of types of each appointment in the given month. */
    public void setScheduleReportsTV(String month)
    {
        // Reset the observable list
        Types.resetTypesByMonth();

        // Get the month
        getMonth(month);

        // Get all the appointments happening in the chosen month
        ObservableList<Appointment> relevantApps = FXCollections.observableArrayList();
        relevantApps.clear();
        for (Appointment appointment : Appointment.getAllAppointments()) {
            int m = appointment.getStart().getMonth().getValue();
            if (m == monthValue) { relevantApps.add(appointment); }
        }

        // Find the number of types
        for (String type : types)
        {
            int num = 0;
            for (Appointment appointment : relevantApps) { if (appointment.getType().equals(type)) { num++; } }
            // Create a new type and add it to the list
            Types types = new Types(type, num);
            Types.addType(types);
        }
    }

    /** This method sets the contact schedule tableview to reflect all the appointments for a given contact. */
    public void setContactScheduleTV(Contact contact)
    {
        // Add all the appointments for the chosen contact
        Appointment.addAppointmentsByContact(contact);
    }
}