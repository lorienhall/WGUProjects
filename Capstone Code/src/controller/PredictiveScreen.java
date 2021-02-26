package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Logistic;
import model.Review;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class PredictiveScreen
{
    /** Setting the fx id references for the label and buttons. */
    @FXML
    private Button BackB;
    @FXML
    private Button EnterB;
    public javafx.scene.control.Label SatisfactionChanceL;

    /** Setting the fx id references for the text fields and combo boxes. */
    @FXML
    private TextField AgeTF;
    @FXML
    private TextField DistanceTF;
    @FXML
    private ComboBox<String> TravelTypeCB;
    @FXML
    private ComboBox<String> ClassCB;

    Stage stage;
    Parent scene;
    Logistic logistic = new Logistic(5);

    public void initialize() throws FileNotFoundException
    {
        // Set data for the combo boxes
        TravelTypeCB.setItems(Review.getObTravelType());
        ClassCB.setItems(Review.getObClass());

        List<Logistic.Instance> dataSet = Logistic.readDataSet();
        logistic.train(dataSet);

    }

    public void back(MouseEvent mouseEvent) throws IOException
    {
        // Load the dashboard
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method gets the satisfaction prediction based on the entered data. */
    public void enter(MouseEvent mouseEvent)
    {
        int[][] x = new int[4][4];
        // Check that nothing is empty and that the text fields contain only numbers
        if (AgeTF.getText().trim().isEmpty())
        { error(1); return; }
        else if (!AgeTF.getText().trim().matches("-?\\d+(\\.\\d+)?"))
        { error(5); return; }
        if (TravelTypeCB.getSelectionModel().getSelectedItem() == null)
        { error(2); return; }
        if (ClassCB.getSelectionModel().getSelectedItem() == null)
        { error(3); return; }
        if (DistanceTF.getText().trim().isEmpty())
        { error(4); return; }
        else if (!DistanceTF.getText().trim().matches("-?\\d+(\\.\\d+)?"))
        { error(6); return; }


        // Add the gathered data to the variable (age, type of travel, class, flight distance)
        x[0][0] = Integer.parseInt(AgeTF.getText().trim());
        x[3][0] = Integer.parseInt(DistanceTF.getText().trim());
        if (TravelTypeCB.getSelectionModel().getSelectedItem().equals("Business travel"))
        { x[1][0] = 0; }
        else { x[1][0] = 1; }
        if (ClassCB.getSelectionModel().getSelectedItem().equals("Business"))
        { x[2][0] = 0; }
        else if (ClassCB.getSelectionModel().getSelectedItem().equals("Eco"))
        { x[2][0] = 1; }
        else { x[2][0] = 2; }

        double prediction = logistic.getProbability(logistic.prediction(x));
        SatisfactionChanceL.setText(String.valueOf(prediction));
    }

    /** This method provides the error messages. */
    public void error(int code)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Cannot Predict Satisfaction");

        if (code == 1)
        {
            alert.setHeaderText("Empty Field!");
            alert.setContentText("Please enter the customers age.");
        }
        if (code == 2)
        {
            alert.setHeaderText("Empty Field!");
            alert.setContentText("Please enter a travel type.");
        }
        if (code == 3)
        {
            alert.setHeaderText("Empty Field!");
            alert.setContentText("Please enter a class.");
        }
        if (code == 4)
        {
            alert.setHeaderText("Empty Field!");
            alert.setContentText("Please enter a flight distance.");
        }
        if (code == 5)
        {
            alert.setHeaderText("Age must be a number!");
            alert.setContentText("Please enter a number for the age. Ex 40, 23");
        }
        if (code == 6)
        {
            alert.setHeaderText("Flight distance must be a number!");
            alert.setContentText("Please enter a number for the flight distance. Ex 4000, 23344");
        }

        alert.showAndWait();
    }

}
