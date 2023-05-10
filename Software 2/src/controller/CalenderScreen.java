package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

/** This is the controller class for the calender screen. */
public class CalenderScreen implements Initializable
{

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
    @FXML
    private TableColumn<Appointment, String> DescriptionTC;
    @FXML
    private TableColumn<Appointment, String> LocationTC;
    
    /** Setting the fx id references for the radio buttons, toggle group and label. */
    @FXML
    private RadioButton MonthlyVRB;
    @FXML
    private ToggleGroup viewType;
    @FXML
    private RadioButton WeeklyVRB;
    @FXML
    private RadioButton AllAppsRB;
    public Label TimezoneL;

    /** The next two variables are for scene switching. */
    Stage stage;
    Parent scene;

    /** This method initializes the class and sets things up to carry the data it needs. */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // Set data for the schedule table
        ScheduleTV.setItems(Appointment.getAllAppointments());
        addMonths();

        AppIDTC.setCellValueFactory(new PropertyValueFactory<>("appid"));
        TitleTC.setCellValueFactory(new PropertyValueFactory<>("title"));
        TypeTC.setCellValueFactory(new PropertyValueFactory<>("type"));
        StartTC.setCellValueFactory(new PropertyValueFactory<>("start"));
        EndTC.setCellValueFactory(new PropertyValueFactory<>("end"));
        CustIDTCapp.setCellValueFactory(new PropertyValueFactory<>("custid"));
        DescriptionTC.setCellValueFactory(new PropertyValueFactory<>("description"));
        LocationTC.setCellValueFactory(new PropertyValueFactory<>("location"));

        // Display the timezone
        displayTimezone();
    }

    /** These variables are for changing the tableview to display the appointments by week or by month. */
    ObservableList<Appointment> appList = FXCollections.observableArrayList();
    ObservableList<Integer> months = FXCollections.observableArrayList();
    ZonedDateTime oneWeekLater = null;
    ZonedDateTime weekStart = null;
    Boolean checkweek = true;

    /** This method adds the values for each month to the observable list months. */
    private void addMonths()
    {
        months.add(1);
        months.add(2);
        months.add(3);
        months.add(4);
        months.add(5);
        months.add(6);
        months.add(7);
        months.add(8);
        months.add(9);
        months.add(10);
        months.add(11);
        months.add(12);
    }

    /** This method determines what is the first month with appointments and then returns it. */
    public int getTheMonth()
    {
        // Get the months
        int monthNum;
        Appointment fApp;
        // Get the earliest appointment
        fApp = getFirst();

        // Get the month for the earliest appointment
        if (fApp != null)
        {
            monthNum = fApp.getStart().getMonthValue();
            return monthNum;
        }

       return 0;
    }

    /** This method sets the applist to be the appointments in the given month. */
    public void setMonthlyView(int monthNum)
    {
        if (MonthlyVRB.isSelected())
        {
            appList.clear();
            // Get the appointments in that month
            for (Appointment appointment : Appointment.getAllAppointments())
            {
                int appS = appointment.getStart().getMonthValue();
                if (appS == monthNum)
                {
                    appList.add(appointment);
                }
            }
        }
    }

    /** This method determines what the first appointment is and then returns it. */
    public Appointment getFirst()
    {
        // Get the first appointment
        Appointment firstApp = null;
        for (Appointment appointment : Appointment.getAllAppointments())
        {
            if (firstApp == null) { firstApp = appointment; }
            if (firstApp.getStart().isAfter(appointment.getStart())) { firstApp = appointment; }
        }
        return firstApp;
    }

    /** This method gets the difference between the first and last appointment and returns it as the max number of weeks. */
    public int getMax ()
    {
        Appointment lastApp = getFirst();
        // Get the last appointment
        for (Appointment appointment : Appointment.getAllAppointments())
        {
            if (appointment.getStart().isAfter(lastApp.getStart()))
            {
                lastApp = appointment;
            }
        }

        // Get the limit for the for loop that gets the next week
        if (lastApp != null)
        {
            for (int i = 0; i < 100; i++)
            {
                if (lastApp.getStart().minusWeeks(i).isBefore(getFirst().getStart()))
                {
                    return i;
                }
            }
        }
        return 0;
    }

    /** This method sets the zoned date times used to set the weekly view to be before and after the first appointment. */
    public void setZDTs ()
    {
        if (checkweek)
        {
            Appointment firstApp = getFirst();
            if (firstApp.getStart().getDayOfWeek().getValue() == 1)
            {
                weekStart = firstApp.getStart();
                oneWeekLater = weekStart.plusWeeks(1);
                checkweek = false;
            }
            else if (firstApp.getStart().getDayOfWeek().getValue() != 1)
            {
                int fd = firstApp.getStart().getDayOfWeek().getValue() - 1;
                weekStart = firstApp.getStart().minusDays(fd);
                oneWeekLater = weekStart.plusWeeks(1);
                checkweek = false;
            }
        }
    }

    /** This method sets the applist to be the appointments in the given week. */
    public void setWeeklyView (ZonedDateTime weekS)
    {
        if (WeeklyVRB.isSelected())
        {
            // Get the appointments in that week
            appList.clear();
            for (Appointment appointment : Appointment.getAllAppointments())
            {
                if (appointment.getStart().isBefore(oneWeekLater) && appointment.getStart().isAfter(weekS)) { appList.add(appointment); }
            }

        }
    }

    /** This method sets the tableview to display the appointments for the next week or month when the next button on the screen is clicked. */
    public void nextWeekorMonth(MouseEvent mouseEvent)
    {
        if (MonthlyVRB.isSelected())
        {
            for (Appointment app : appList)
            {
                if (app.getStart().getMonth().getValue() == 12)
                {
                    setMonthlyView(getTheMonth());
                    return;
                }
            }
            for (int monthNum = getTheMonth() + 1; monthNum < months.size()+1; monthNum++ )
            {
                setMonthlyView(monthNum);
                if (!appList.isEmpty())
                {
                    ScheduleTV.setItems(appList);
                    return;
                }
            }
        }

        if (WeeklyVRB.isSelected())
        {
            int max = getMax();
            // Get the appointments for the next week that has appointments
            for (int plusWeeks = 1; plusWeeks < max + 2; plusWeeks++)
            {

                if (plusWeeks == max + 1)
                {
                    checkweek = true;
                    setZDTs();
                    setWeeklyView(weekStart);
                    if (!appList.isEmpty())
                    {
                        ScheduleTV.setItems(appList);
                        return;
                    }
                }

                weekStart = weekStart.plusWeeks(1);
                oneWeekLater = oneWeekLater.plusWeeks(1);
                setWeeklyView(weekStart);

                if (!appList.isEmpty())
                {
                    ScheduleTV.setItems(appList);
                    return;
                }

            }
        }

        if (AllAppsRB.isSelected())
        {
            // Do nothing
            ScheduleTV.setItems(Appointment.getAllAppointments());
        }
    }

    /** This method loads the add appointment screen when the add appointment button is clicked on the screen. */
    public void addAppointment(MouseEvent mouseEvent) throws IOException
    {
        // Load the add appointment screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddAppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method loads the modify appointment screen when the modify appointment button is clicked on the screen. */
    public void modifyAppointment(MouseEvent mouseEvent) throws IOException
    {
        // Validate something is selected


        // Load the modify appointment screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ModifyAppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method deletes the selected appointment when the delete appointment button is clicked on the screen. */
    public void deleteAppointment(MouseEvent mouseEvent)
    {
        // Validate something is selected
        if (ScheduleTV.getSelectionModel().getSelectedItem() == null)
        {
            MainMenu.errorAppointment(8,null);
            return;
        }
        Appointment app = ScheduleTV.getSelectionModel().getSelectedItem();

        // Display confirmation message
        if (!MainMenu.confirmationWindows(1, app, null)) { return; }

        // Delete appointment
        Appointment.deleteAppointment(app);
    }

    /** This method loads the main menu when the exit button on the screen is pressed. */
    public void toMainMenu(MouseEvent mouseEvent) throws IOException
    {
        // Load the main screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method sets timezone label to reflect the system timezone. */
    public void displayTimezone ()
    {
        TimezoneL.setText(LoginScreen.getCurrentTZ().getDisplayName());
    }

    /** This method sets the tableview to display all the appointments. */
    public void viewAll(MouseEvent mouseEvent) { ScheduleTV.setItems(Appointment.getAllAppointments()); }

    /** This method sets the tableview to display all the appointments by month. */
    public void setMonthlyV(MouseEvent mouseEvent)
    {
        setMonthlyView(getTheMonth());
        ScheduleTV.setItems(appList);
    }

    /** This method sets the tableview to display all the appointments by week.*/
    public void setWeeklyV(MouseEvent mouseEvent)
    {
        checkweek = true;
        setZDTs();
        setWeeklyView(weekStart);
        ScheduleTV.setItems(appList);
    }
}

