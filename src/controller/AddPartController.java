package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/** This is the controller class for the Add Part screen. */
public class AddPartController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    private static int ID = 6;
    Stage stage;
    Parent scene;

    /** Misc declarations*/
    public ToggleGroup AddPart;

    public Button SaveB;
    public Button CancelB;

    public Label CompanyNameL;
    public Label MachineIDL;

    @FXML
    private RadioButton InHouseRB;
    @FXML
    private RadioButton OutSourcedRB;

    /** Setting the fx id references for the textfields*/
    @FXML
    private TextField APartID;

    @FXML
    private TextField APartName;

    @FXML
    private TextField APartInv;

    @FXML
    private TextField APartPr;

    @FXML
    private TextField APartMax;

    @FXML
    private TextField APartIDName;

    @FXML
    private TextField APartMin;

    /** This method saves the information entered into the textfields as a new part
     * and then returns to the main screen. It also validates the data and generates error messages. */
    public void savePart(javafx.event.ActionEvent actionEvent) throws IOException
    {
        // Reset field styles
        resetFieldStyles();

        // Validate data
        if (!validateData())
        {
            return;
        }

        // Get the information from the textfields
        int id = ID;
        String name = APartName.getText();
        int stock = Integer.parseInt(APartInv.getText());
        double price = Double.parseDouble(APartPr.getText());
        int min = Integer.parseInt(APartMin.getText());
        int max = Integer.parseInt(APartMax.getText());

        if(InHouseRB.isSelected())
        {
            int machineId = Integer.parseInt(APartIDName.getText());
            // Validate machine id
            if (!Pattern.matches("[0-9]+", String.valueOf(machineId)))
            {
                MainScreenController.errorPart(9, APartIDName);
                return;
            }

            Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineId));
            ID++;
        }

        if(OutSourcedRB.isSelected())
        {
            String companyName = APartIDName.getText();

            Inventory.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
            ID++;
        }

        // Return to the main screen
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Main_Screen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method contains all the checks to ensure all the data is correctly entered.*/
    private boolean validateData()
    {
        // Ensure nothing is empty
        if(APartIDName.getText().trim().isEmpty())
        {
            MainScreenController.errorPart(1, APartIDName);
            return false;
        }
        if (APartInv.getText().trim().isEmpty())
        {
            MainScreenController.errorPart(1, APartInv);
            return false;
        }
        if (APartPr.getText().trim().isEmpty())
        {
            MainScreenController.errorPart(1, APartPr);
            return false;
        }
        if (APartMin.getText().trim().isEmpty())
        {
            MainScreenController.errorPart(1, APartMin);
            return false;
        }
        if (APartMax.getText().trim().isEmpty())
        {
            MainScreenController.errorPart(1, APartMax);
            return false;
        }
        if (APartIDName.getText().trim().isEmpty())
        {
            MainScreenController.errorPart(1,APartIDName);
            return false;
        }
        // Get the information from the textfields
        String name = APartName.getText();
        int stock = Integer.parseInt(APartInv.getText());
        double price = Double.parseDouble(APartPr.getText());
        int min = Integer.parseInt(APartMin.getText());
        int max = Integer.parseInt(APartMax.getText());

        // Check that there are no negatives
        if (min < 0)
        {
            MainScreenController.errorPart(5, APartMin);
            return false;
        }
        if (max < 0)
        {
            MainScreenController.errorPart(5, APartMax);
            return false;
        }
        if (InHouseRB.isSelected())
        {
            if (Integer.parseInt(APartIDName.getText()) < 0)
            {
                MainScreenController.errorPart(5, APartIDName);
                return false;
            }
        }
        if (price < 0)
        {
            MainScreenController.errorPart(5, APartPr);
            return false;
        }
        if (stock < 0)
        {
            MainScreenController.errorPart(5, APartInv);
            return false;
        }
        // Check data types
        if (!Pattern.matches("[0-9]+", String.valueOf(stock)))
        {
            MainScreenController.errorPart(3, APartInv);
            return false;
        }
        if (!Pattern.matches("^(\\d{1,3}(,\\d{3})*|(\\d+))(\\.\\d{2})?$", String.valueOf(price)))
        {
            MainScreenController.errorPart(3, APartPr);
            return false;
        }
        if (!Pattern.matches("[0-9]+", String.valueOf(min)))
        {
            MainScreenController.errorPart(3, APartMin);
            return false;
        }
        if (!Pattern.matches("[0-9]+", String.valueOf(max)))
        {
            MainScreenController.errorPart(3, APartMax);
            return false;
        }
        // Check data in relation to other data
        if (name.trim().isEmpty() || name.trim().toLowerCase().equals("part name"))
        {
            MainScreenController.errorPart(4, APartName);
            return false;
        }
        if (min > max)
        {
            MainScreenController.errorPart(8, APartMin);
            return false;
        }
        if (stock < min)
        {
            MainScreenController.errorPart(6, APartInv);
            return false;
        }
        if (stock > max)
        {
            MainScreenController.errorPart(7, APartInv);
            return false;
        }
        return true;
    }

    /** This method cancels the action of creating a new part by returning to the main screen*/
    public void cancelAction(javafx.event.ActionEvent actionEvent) throws IOException
    {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Main_Screen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /** The two following methods determine if which radio button in the pane is selected and change the
     * textfield label appropriately. */
    // Radio button stuff
    public void setInHouse()
    {
        if (InHouseRB.isSelected())
        {
            CompanyNameL.setVisible(false);
            MachineIDL.setVisible(true);
        }
    }

    public void setOutsourced()
    {

        if (OutSourcedRB.isSelected())
        {
            CompanyNameL.setVisible(true);
            MachineIDL.setVisible(false);
        }
    }

    /** This method resets the field styles back to normal in case they were changed by the error messages.*/
    private void resetFieldStyles()
    {
        try
        {
            APartID.setStyle("-fx-border-color: light gray");
            APartName.setStyle("-fx-border-color: light gray");
            APartInv.setStyle("-fx-border-color: light gray");
            APartPr.setStyle("-fx-border-color: light gray");
            APartMin.setStyle("-fx-border-color: light gray");
            APartMax.setStyle("-fx-border-color: light gray");
        }
        catch (ClassCastException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There are invalid inputs");
            alert.setContentText("Creates exception: " + e);
        }
    }

}
