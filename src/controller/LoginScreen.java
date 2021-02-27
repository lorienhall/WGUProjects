package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.Users;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/** This is the controller class for the login screen. */
public class LoginScreen implements Initializable
{
    /** Setting the fx id references for the text fields. */
    @FXML
    private PasswordField PasswordPF;
    @FXML
    private TextField UserNameTF;

    /** Setting the fx id references for the button. */
    @FXML
    private Button LoginB;

    /** Setting the fx id references for the labels. */
    @FXML
    private Label TimeZoneL;
    @FXML
    private Label CountryL;
    @FXML
    private Label titleTF;
    @FXML
    private Label userNTF;
    @FXML
    private Label passwordTF;

    /** These objects are for screen changing, time/language stuff and data passing. */
    Stage stage;
    Parent scene;
    private static final TimeZone currentTZ = TimeZone.getTimeZone(ZoneId.systemDefault());
    private static final ZonedDateTime current = ZonedDateTime.now();
    public static ZonedDateTime getCurrent () { return current; }
    public static TimeZone getCurrentTZ () { return currentTZ; }
    public static Users user = null;
    Locale here = Locale.getDefault();
    private String lang = here.getDisplayLanguage();

    /** This method initializes the login screen. */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // Display the timezone
        displayTimezone();

        // Change the language
        setLanguage();

    }

    /** This method determines the system language and changes things accordingly. */
    public void setLanguage ()
    {
        if (lang.equals("français"))
        {
            setFrench();
        }
        if (lang.equals("English"))
        {
            setEnglish();
        }
        System.out.println("The System Language = " + lang);
    }

    /** This method sets all the text to french. */
    public void setFrench()
    {
        titleTF.setText("base de données SQL");
        userNTF.setText("Nom d'utilisateur");
        passwordTF.setText("Mot de passe");
        LoginB.setText("S'identifier");
    }

    /** This method sets all the text to english. */
    public void setEnglish()
    {
        titleTF.setText("SQL Database");
        userNTF.setText("User Name");
        passwordTF.setText("Password");
        LoginB.setText("Login");
    }

    /** This method sets timezone label to reflect the system timezone. */
    public void displayTimezone ()
    {
        CountryL.setText(current.getZone().toString());
        TimeZoneL.setText(currentTZ.getDisplayName());
    }

    /** These two methods are to get and store the user who logged in. */
    private static Users cUser = null;
    public static Users getCUser() { return cUser; }

    /** This method executes when the login button is clicked. It checks the password and username and opens the main screen. */
    public void login(MouseEvent mouseEvent) throws IOException
    {
        // Check to make sure they arent empty
        if (UserNameTF.getText().isEmpty())
        {

            if (lang.equals("français"))
            {

                fLoginError(3);
                return;
            }
            if (lang.equals("English"))
            {
                eLoginError(3);
                return;
            }
        }
        if (PasswordPF.getText().isEmpty())
        {
            if (lang.equals("français"))
            {
                fLoginError(4);
                return;
            }
            if (lang.equals("English"))
            {
                eLoginError(4);
                return;
            }
        }
        // Write login attempt to the file
        Boolean check = recordAttempts();

       if (check)
       {
           // Load the main screen
           stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
           scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
           stage.setScene(new Scene(scene));
           stage.show();
       }

    }


    /** This method checks whether or not the entered username and password match a user in the database and provides an alert if it does not. */
    public Boolean checkUser ()
    {
        int UserID = 0;

        for (Users users : Users.getAllUsers()) {
            String username = users.getUser_Name();

            // Check the username
            if (UserNameTF.getText().trim().equals(username)) {
                UserID = users.getUser_ID();
            }
        }
        if (UserID == 0)
        {
            if (lang.equals("français"))
            {
                fLoginError(1);
                return false;
            }
            if (lang.equals("English"))
            {
                eLoginError(1);
                return false;
            }
        }

        // Get user based on the username
        for (Users users : Users.getAllUsers())
        {
            if (users.getUser_ID() == UserID)
            {
                cUser = users;
                user = users;
            }
        }
        if (PasswordPF.getText().trim().equals(cUser.getPassword()))
        {
            return true;
        }
        else if (!PasswordPF.getText().trim().equals(cUser.getPassword()))
        {
            if (lang.equals("français"))
            {
                fLoginError(2);
                PasswordPF.clear();
                return false;
            }
            if (lang.equals("English"))
            {
                eLoginError(2);
                PasswordPF.clear();
                return false;
            }
        }
        return true;
    }

    /** This method records the login attempts and writes them to the login_activity.text file. */
    public boolean recordAttempts () throws IOException
    {
        // Create file writer and print writer objects
        String filename = "login_activity.txt", item;
        FileWriter fwLA = new FileWriter(filename, true);
        PrintWriter loginActivity = new PrintWriter(fwLA);

        // Get string to write to the file
        String time = current.getHour() + ":" + current.getMinute() + ":" + current.getSecond();
        String td = current.toLocalDate().toString() + " " + time + ", " + currentTZ.getDisplayName();
        String s = null;
        String u = null;
        Boolean check = checkUser();

        if (check)
        {
            s = "Successful";
            u = "     User = " + cUser.getUser_Name();
        }
        if (!check && cUser != null)
        {
            s = "Unsuccessful";
            u = "   User = " + cUser.getUser_Name();
        }
        if (!check && cUser == null)
        {
            s = "Unsuccessful";
            u = "   No User Found";
        }


        // Write to the file
        item = s + ": " + td + u;
        loginActivity.println(item);

        // Close the file
        loginActivity.close();
        return check;
    }

    /** This method creates the errors in english that are used in the rest of the controller. */
    public static void eLoginError(int code)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Cannot login");

        if (code == 1)
        {
            alert.setHeaderText("Incorrect Username");
            alert.setContentText("Please retype your username.");
        }
        if (code == 2)
        {
            alert.setHeaderText("Incorrect Password");
            alert.setContentText("Please retype your password.");
        }
        if (code == 3)
        {
            alert.setHeaderText("Empty Field!");
            alert.setContentText("Please enter a username.");
        }
        if (code == 4)
        {
            alert.setHeaderText("Empty Field!");
            alert.setContentText("Please enter a password.");
        }
        alert.showAndWait();
    }

    /** This method creates the errors in french that are used in the rest of the controller. */
    public static  void fLoginError(int code)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Vous ne pouvez pas vous connecter");

        if (code == 1)
        {
            alert.setHeaderText("Nom d'utilisateur incorrect");
            alert.setContentText("Veuillez retaper votre nom d'utilisateur");
        }
        if (code == 2)
        {
            alert.setHeaderText("Mot de passe incorrect");
            alert.setContentText("Veuillez retaper votre mot de passe.");
        }
        if (code == 3)
        {
            alert.setHeaderText("Champ vide!");
            alert.setContentText("Merci d'entrer un nom d'utilisateur.");
        }
        if (code == 4)
        {
            alert.setHeaderText("Champ vide!");
            alert.setContentText("Veuillez entrer un mot de passe.");
        }
        alert.showAndWait();
    }
}
