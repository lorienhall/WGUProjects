package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** This is the controller class for the customer screen. */
public class CustomersScreen implements Initializable
{
    /** Setting the fx id references for the customers table. */
    @FXML
    private TableView<Customer> CustomersTV;
    @FXML
    private TableColumn<Customer, Integer> CustIDTC;
    @FXML
    private TableColumn<Customer, String> NameTC;
    @FXML
    private TableColumn<Customer, String> PhoneTC;
    @FXML
    private TableColumn<Customer, String> AddressTC;
    @FXML
    private TableColumn<Customer, String> CountryTC;
    @FXML
    private TableColumn<Customer, String> FirstLevelTC;

    /** Setting the fx id references for the buttons. */
    @FXML
    private Button AddCustB;
    @FXML
    private Button ModifyCustB;
    @FXML
    private Button DeleteCustB;
    @FXML
    private Button ExitB;

    /** The next two variables are for scene switching. */
    Stage stage;
    Parent scene;

    /** This method initializes the class and sets things up to carry the data it needs. */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // Set data for the customers table
        CustomersTV.setItems(Customer.getAllCustomers());

        CustIDTC.setCellValueFactory(new PropertyValueFactory<>("custid"));
        NameTC.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        PhoneTC.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        AddressTC.setCellValueFactory(new PropertyValueFactory<>("address"));
        CountryTC.setCellValueFactory(new PropertyValueFactory<>("country"));
        FirstLevelTC.setCellValueFactory(new PropertyValueFactory<>("firstLevel"));
    }

    /** This method loads the add customer screen. */
    public void addCustomer(MouseEvent mouseEvent) throws IOException
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
        // Validate something is selected
        if (CustomersTV.getSelectionModel().getSelectedItem() == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Nothing Selected!");
            alert.setContentText("Please select a customer to modify,");
            alert.showAndWait();
        }

        // Load the modify customer screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ModifyCustomerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method deletes the selected customer. */
    public void deleteCustomer(MouseEvent mouseEvent)
    {
        // Validate something is selected
        if (CustomersTV.getSelectionModel().getSelectedItem() == null)
        {
            MainMenu.errorCustomer(3,null);
            return;
        }
        Customer cust = CustomersTV.getSelectionModel().getSelectedItem();

        // Check the customer has no appointments
        for (Appointment appointment : Appointment.getAllAppointments())
        {
            if (cust.getCustid() == appointment.getCustid())
            {
                MainMenu.errorCustomer(4,null);
                return;
            }
        }

        // Display confirmation message
        if (!MainMenu.confirmationWindows(1,null, cust)) { return; }

        // Delete customer
        Customer.deleteCustomer(cust);
    }

    /** This method loads the main menu screen. */
    public void toMainMenu(MouseEvent mouseEvent) throws IOException
    {
        // Load the main screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}