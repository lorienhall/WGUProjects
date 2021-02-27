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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static java.time.ZoneOffset.UTC;

/** This is the controller class for the modify customer screen. */
public class ModifyCustomerScreen implements Initializable
{
    /** Setting the fx id references for the buttons. */
    @FXML
    private Button CancelB;
    @FXML
    private Button SaveB;

    /** Setting the fx id references for the textfields. */
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

    /** Setting the fx id references for the choice box*/
    @FXML
    private ComboBox<FirstLevelDivisions> FirstLDCB;
    @FXML
    private ComboBox<Country> CountryCB;

    /** The next few variables are for scene switching and time conversions. */
    Stage stage;
    Parent scene;
    private Customer cust;
    TimeZone tz = LoginScreen.getCurrentTZ();

    /** This method initializes the class and sets things up to carry the data it needs.
     * It also has a lambda expression. I chose to use a lambda expression there because
     * I only need the new selection long enough to set the first level division combo
     * box to reflect that choice. */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // Get data from the main screen
        cust = MainMenu.getmodifyCust();

        // Set data for the combo boxes
        CountryCB.setItems(Country.getAllCountries());

        CountryCB.setValue(Country.searchCountryS(cust.getCountry()));
        FirstLDCB.setValue(FirstLevelDivisions.searchFirstLevel(cust.getFirstLevel()));

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

        // Display customer data
        CustIDTF.setText(String.valueOf(cust.getCustid()));
        PostalCTF.setText(cust.getPostalCode());
        AddressTF.setText(cust.getAddress());
        FullNTF.setText(cust.getFullName());
        PhoneTF.setText(cust.getPhoneNumber());
        for (Country country : Country.getAllCountries())
        {
            if (country.getCountry().equals(cust.getCountry()))
            {
                CountryCB.setValue(country);
                FirstLDCB.setItems(FirstLevelDivisions.getFirstLDByCountry(country));
            }
        }
        for (FirstLevelDivisions firstLevelD : FirstLevelDivisions.getFirstLevelD())
        {
            if (firstLevelD.getDivision().equals(cust.getFirstLevel()))
            {
                FirstLDCB.setValue(firstLevelD);
            }
        }
    }

    /** This method saves the entered data by updating the customer and returns to the main screen. */
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
        int custid = cust.getCustid();
        String fullName = FullNTF.getText().trim();
        String phone = PhoneTF.getText().trim();
        String address = AddressTF.getText().trim();
        String postalCode = PostalCTF.getText().trim();
        int countryid = CountryCB.getSelectionModel().getSelectedItem().getCountry_ID();
        int firstLevelid = FirstLDCB.getSelectionModel().getSelectedItem().getDivision_ID();
        ZonedDateTime lastupdate = ZonedDateTime.ofInstant(LocalDateTime.now(), ZoneOffset.ofHours(UTC.getTotalSeconds()), tz.toZoneId());
        Timestamp Last_Update = Timestamp.valueOf(lastupdate.withZoneSameInstant(UTC).toLocalDateTime());
        String Last_Updated_By = LoginScreen.getCUser().getUser_Name();

        // Update the customer
        Customer.updateCustomer(custid, fullName, address, countryid,firstLevelid, postalCode, phone, Last_Update, Last_Updated_By);

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
            MainMenu.errorCustomer(1, FullNTF);
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

    /** This method loads the main screen. */
    public void cancel(MouseEvent mouseEvent) throws IOException
    {
        // Load the main screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}