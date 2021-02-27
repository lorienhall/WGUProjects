package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.FirstLevelDivisions;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static java.time.ZoneOffset.UTC;

/** This is the controller class for the add customer screen. */
public class AddCustomerScreen implements Initializable
{
    /** Setting the fx id references for the buttons*/
    @FXML
    private Button CancelB;
    @FXML
    private Button SaveB;

    /** Setting the fx id references for the text fields*/
    @FXML
    private TextField CustIDTF;
    @FXML
    private TextField AddressTF;
    @FXML
    private TextField PostalCTF;
    @FXML
    private TextField FullNTF;
    @FXML
    private TextField PhoneTF;

    /** Setting the fx id references for the combo box*/
    @FXML
    private ComboBox<FirstLevelDivisions> FirstLDCB;
    @FXML
    private ComboBox<Country> CountryCB;

    /** The next few variables are for scene switching and time conversions. */
    Stage stage;
    Parent scene;
    TimeZone tz = LoginScreen.getCurrentTZ();

    /** This method initializes the class and sets things up to carry the data it needs.
     * It also has a lambda expression. I chose to use a lambda expression there because
     * I only need the new selection long enough to set the first level division combo
     * box to reflect that choice. */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // Set data for the combo boxes
        FirstLDCB.setItems(FirstLevelDivisions.getFirstLevelD());
        CountryCB.setItems(Country.getAllCountries());

        CountryCB.setPromptText("Choose a country...");
        FirstLDCB.setPromptText("Choose a country first...");

        // Set first level division choice box to display the divisions for the chosen country
        CountryCB.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
        {
            if (newSelection != null)
            {
                FirstLDCB.setPromptText("Choose a division...");
                FirstLDCB.setItems(FirstLevelDivisions.getFirstLDByCountry(newSelection));
            }
        } );
    }

    /** This method saves the entered data as a new customer and returns to the main screen when the save button on the screen is pressed. */
    public void saveCustomer(MouseEvent mouseEvent) throws IOException
    {
        // Reset field styles in case they were changed by an error message
        resetFieldStyles();

        // Validate data
        if (!validateData())
        {
            return;
        }
        // Gather the data
        String fullName = FullNTF.getText().trim();
        String phone = PhoneTF.getText().trim();
        String address = AddressTF.getText().trim();
        String postalCode = PostalCTF.getText().trim();
        String country = String.valueOf(CountryCB.getSelectionModel().getSelectedItem().getCountry());
        String firstLevel = String.valueOf(FirstLDCB.getSelectionModel().getSelectedItem().getDivision());
        ZonedDateTime createDate = ZonedDateTime.ofInstant(LocalDateTime.now(), ZoneOffset.ofHours(UTC.getTotalSeconds()), tz.toZoneId());
        Timestamp Create_Date = Timestamp.valueOf(createDate.withZoneSameInstant(UTC).toLocalDateTime());
        String Created_By = LoginScreen.getCUser().getUser_Name();
        ZonedDateTime lastupdate = ZonedDateTime.ofInstant(LocalDateTime.now(), ZoneOffset.ofHours(UTC.getTotalSeconds()), tz.toZoneId());
        Timestamp Last_Update = Timestamp.valueOf(lastupdate.withZoneSameInstant(UTC).toLocalDateTime());
        String Last_Updated_By = LoginScreen.getCUser().getUser_Name();

        // Create new customer
        Customer.newCustomer(fullName,address,country,firstLevel,postalCode,phone,Create_Date,
                Created_By,Last_Update,Last_Updated_By);

        // Return to main menu
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method contains all the checks to ensure all the data is correctly entered.*/
    private boolean validateData()
    {
        // Make sure nothing is empty
        if (FullNTF.getText().isEmpty())
        {
            MainMenu.errorCustomer(1, FullNTF);
            return false;
        }
        if (PhoneTF.getText().isEmpty())
        {
            MainMenu.errorCustomer(1, PhoneTF);
            return false;
        }
        if (AddressTF.getText().isEmpty())
        {
            MainMenu.errorCustomer(1, AddressTF);
            return false;
        }
        if (PostalCTF.getText().isEmpty())
        {
            MainMenu.errorCustomer(1, PostalCTF);
            return false;
        }
        // Gather the data
        String phone = PhoneTF.getText().trim();

        // Validate the phone number
        if (!Pattern.matches("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$", phone))
        {
            MainMenu.errorCustomer(5, PhoneTF);
            return false;
        }
        return true;
    }

    /** This method resets the field styles back to normal in case they were changed by the error messages.*/
    private void resetFieldStyles()
    {
        FullNTF.setStyle("-fx-border-color: light gray");
        PhoneTF.setStyle("-fx-border-color: light gray");
        PostalCTF.setStyle("-fx-border-color: light gray");
        AddressTF.setStyle("-fx-border-color: light gray");
    }

    /** This method returns to the main menu when the cancel button on the screen is pressed. */
    public void cancel(javafx.scene.input.MouseEvent mouseEvent) throws IOException
    {
        // Load the main screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

}
