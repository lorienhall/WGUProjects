package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/** This is the controller for the modify part screen. */
public class ModifyPartController implements Initializable {

    private Part part = null;
    Stage stage;
    Parent scene;

    /** This is the initialize method. It sets the textfields to the necessary data. */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Get data from the main screen
        part = MainScreenController.getmodifyPart();

        // Display part data
        MPartID.setText(String.valueOf(part.getId()));
        MPartName.setText(part.getName());
        MPartInv.setText(String.valueOf(part.getStock()));
        MPartPr.setText(String.valueOf(part.getPrice()));
        MPartMax.setText(String.valueOf(part.getMax()));
        MPartMin.setText(String.valueOf(part.getMin()));
        if (part instanceof InHouse)
        {
            InHouseRB.setSelected(true);
            MPartIDName.setText(String.valueOf(((InHouse) part).getMachineId()));
        }
        if (part instanceof Outsourced)
        {
            OutSourcedRB.setSelected(true);
            MPartIDName.setText(String.valueOf(((Outsourced) part).getCompanyName()));
        }

    }

    /** Setting the fx id references for the buttons, labels and radio buttons. */
    @FXML
    private RadioButton InHouseRB;

    @FXML
    private RadioButton OutSourcedRB;

    public Label CompanyNameL;
    public Label MachineIDL;


    public ToggleGroup MPart;

    public Button SaveB;
    public Button CancelB;

    /** Setting the fx id references for the textfields. */
    @FXML
    private TextField MPartID;

    @FXML
    private TextField MPartName;

    @FXML
    private TextField MPartInv;

    @FXML
    private TextField MPartPr;

    @FXML
    private TextField MPartMax;

    @FXML
    private TextField MPartIDName;

    @FXML
    private TextField MPartMin;


    /** The two following methods determine if which radio button in the pane is selected and change the
     * textfield label appropriately. */
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

    /** This method saves the information entered into the textfields as a new part, deletes the old part,
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

        // Check in house vs outsourced
        if (InHouseRB.isSelected())
        {
            // Validate machine id
            if (!Pattern.matches("[0-9]+", MPartIDName.getText()))
            {
                MainScreenController.errorPart(9, MPartIDName);
                return;
            }
            // Update part
            updateItemInHouse();

            // Load main screen
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/Main_Screen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
            return;
        }
        if (OutSourcedRB.isSelected())
        {
            // Update part
            updateItemOutsourced();

            // Load main screen
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/Main_Screen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /** This method contains all the checks to ensure all the data is correctly entered. */
    private boolean validateData()
    {
        // Ensure nothing is empty
        if(MPartName.getText().isEmpty())
        {
            MainScreenController.errorPart(1, MPartName);
            return false;
        }
        if(MPartIDName.getText().trim().isEmpty())
        {
            MainScreenController.errorPart(1, MPartIDName);
            return false;
        }
        if (MPartInv.getText().trim().isEmpty())
        {
            MainScreenController.errorPart(1, MPartInv);
            return false;
        }
        if (MPartPr.getText().trim().isEmpty())
        {
            MainScreenController.errorPart(1, MPartPr);
            return false;
        }
        if (MPartMin.getText().trim().isEmpty())
        {
            MainScreenController.errorPart(1, MPartMin);
            return false;
        }
        if (MPartMax.getText().trim().isEmpty())
        {
            MainScreenController.errorPart(1, MPartMax);
            return false;
        }
        // Get the new data from the text fields
        String name = MPartName.getText().trim();
        int stock = Integer.parseInt(MPartInv.getText().trim());
        double price = Double.parseDouble(MPartPr.getText().trim());
        int min = Integer.parseInt(MPartMin.getText().trim());
        int max = Integer.parseInt(MPartMax.getText().trim());
        // Check that there are no negatives
        if (min < 0)
        {
            MainScreenController.errorPart(5, MPartMin);
            return false;
        }
        if (max < 0)
        {
            MainScreenController.errorPart(5, MPartMax);
            return false;
        }
        if (InHouseRB.isSelected())
        {
            if (Integer.parseInt(MPartIDName.getText()) < 0)
            {
                MainScreenController.errorPart(5, MPartIDName);
                return false;
            }
        }
        if (price < 0)
        {
            MainScreenController.errorPart(5, MPartPr);
            return false;
        }
        if (stock < 0)
        {
            MainScreenController.errorPart(5, MPartInv);
            return false;
        }
        // Check data types
        if (!Pattern.matches("[0-9]+", String.valueOf(stock)))
        {
            MainScreenController.errorPart(3, MPartInv);
            return false;
        }
        if (!Pattern.matches("^(\\d{1,3}(,\\d{3})*|(\\d+))(\\.\\d{2})?$", String.valueOf(price)))
        {
            MainScreenController.errorPart(3, MPartPr);
            return false;
        }
        if (!Pattern.matches("[0-9]+", String.valueOf(min)))
        {
            MainScreenController.errorPart(3, MPartMin);
            return false;
        }
        if (!Pattern.matches("[0-9]+", String.valueOf(max)))
        {
            MainScreenController.errorPart(3, MPartMax);
            return false;
        }
        // Check data in relation to other data
        if (name.trim().isEmpty() || name.trim().toLowerCase().equals("part name"))
        {
            MainScreenController.errorPart(4, MPartName);
            return false;
        }
        if (min > max)
        {
            MainScreenController.errorPart(8, MPartMin);
            return false;
        }
        if (stock < min)
        {
            MainScreenController.errorPart(6, MPartInv);
            return false;
        }
        if (stock > max)
        {
            MainScreenController.errorPart(7, MPartInv);
            return false;
        }
        return true;
    }

    /** The next two methods update the part depending on whether its an in-house or an outsourced. */
    private void updateItemInHouse()
    {
        Inventory.updatePart(new InHouse(Integer.parseInt(MPartID.getText().trim()),MPartName.getText().trim(),
                Double.parseDouble(MPartPr.getText().trim()), Integer.parseInt(MPartInv.getText().trim()),
                Integer.parseInt(MPartMin.getText().trim()), Integer.parseInt(MPartMax.getText().trim()),Integer.parseInt(MPartIDName.getText())));
    }

    private void updateItemOutsourced()
    {
        Inventory.updatePart(new Outsourced(Integer.parseInt(MPartID.getText().trim()),MPartName.getText().trim(),
                Double.parseDouble(MPartPr.getText().trim()), Integer.parseInt(MPartInv.getText().trim()),
                Integer.parseInt(MPartMin.getText().trim()), Integer.parseInt(MPartMax.getText().trim()),MPartIDName.getText()));
    }

    /** This method cancels the action of creating a new part by returning to the main screen*/
    public void cancelAction(javafx.event.ActionEvent actionEvent) throws IOException
    {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Main_Screen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method resets the field styles back to normal in case they were changed by the error messages.*/
    private void resetFieldStyles()
    {
        MPartID.setStyle("-fx-border-color: light gray");
        MPartName.setStyle("-fx-border-color: light gray");
        MPartInv.setStyle("-fx-border-color: light gray");
        MPartPr.setStyle("-fx-border-color: light gray");
        MPartMin.setStyle("-fx-border-color: light gray");
        MPartMax.setStyle("-fx-border-color: light gray");
    }

}
