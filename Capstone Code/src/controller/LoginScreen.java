package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Account;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LoginScreen
{
    /** Setting the fx id references for the button and fields. */
    @FXML
    private TextField UserTF;
    @FXML
    private PasswordField PassPF;
    @FXML
    private Button LoginB;

    Stage stage;
    Parent scene;

    public void initialize() throws FileNotFoundException
    {
        // Get the accounts and create the objects
        File file =
                new File("C:\\Users\\LorienHall\\Desktop\\WGU\\Capstone\\Capstone Code\\src\\main\\Accounts");
        Scanner sc = new Scanner(file);
        sc.useDelimiter(",");
        sc.next(); // skip the headers

        String username = sc.next().trim();
        String password = sc.next().trim();
        Account account = new Account(username,password);
        Account.setUsers(username);
    }

    public void login(MouseEvent mouseEvent) throws IOException
    {
        // Check that nothing is empty
        if (UserTF.getText().trim().isEmpty())
        {
            LoginError(1);
            return;
        }
        else if (PassPF.getText().trim().isEmpty())
        {
            LoginError(2);
            return;
        }

        // Check the username and password
        String user = UserTF.getText().trim();
        String pass = PassPF.getText().trim();
        if (Account.getUsers().contains(user))
        {
            if (Account.getPassFromUser(user).equals(pass))
            {
                // Load the dashboard
                stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            else { LoginError(4); PassPF.clear(); }

        }
        else { LoginError(3); }

    }

    /** This method creates the errors that are used in the rest of the controller. */
    public static void LoginError(int code)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Cannot login");

        if (code == 1)
        {
            alert.setHeaderText("Empty Field!");
            alert.setContentText("Please enter a username.");
        }
        if (code == 2)
        {
            alert.setHeaderText("Empty Field!");
            alert.setContentText("Please enter a password.");
        }
        if (code == 3)
        {
            alert.setHeaderText("Incorrect Username");
            alert.setContentText("Please retype your username.");
        }
        if (code == 4)
        {
            alert.setHeaderText("Incorrect Password");
            alert.setContentText("Please retype your password.");
        }
        alert.showAndWait();
    }
}
