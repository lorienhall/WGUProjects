package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Descriptive;
import model.Review;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** This is the controller class for the dashboard. */
public class Dashboard
{
    /** Setting the fx id references for the charts. */
    @FXML
    private PieChart TravelPC;
    @FXML
    private PieChart CustTypePC;
    @FXML
    private PieChart ClassPC;
    @FXML
    private BarChart<String, Integer> BarChart;
    @FXML
    private ScatterChart<?, ?> ScatterChart;


    /** Setting the fx id references for the buttons. */
    @FXML
    private Button LogOutB;
    @FXML
    private Button VImprovementsB;
    @FXML
    private Button PreSatisfactionB;
    @FXML
    private Button RSorterB;

    /** Setting the fx id references for the misc items. */
    @FXML
    private ComboBox<String> BarGraphCB;
    @FXML
    public javafx.scene.control.Label DisplayLabel;

    Stage stage;
    Parent scene;

    private final ObservableList<String> graphOptions = FXCollections.observableArrayList("Average Delay","Age vs Flight Distance");
    private final ObservableList<PieChart.Data> travelData = FXCollections.observableArrayList();
    private final ObservableList<PieChart.Data> custTypeData = FXCollections.observableArrayList();
    private final ObservableList<PieChart.Data> classData = FXCollections.observableArrayList();
    int total = 0;
    List<Float> satisfaction = new ArrayList<>();
    List<Float> typeofTravel = new ArrayList<>();
    List<Float> custType = new ArrayList<>();
    List<Float> classs = new ArrayList<>();

    /** This method initializes the screen. It gets and adds data so that the dashboard displays everything correctly. */
    public void initialize() throws FileNotFoundException
    {
        // Get all the reviews
        File file =
                new File("C:\\Users\\LorienHall\\Desktop\\WGU\\Capstone\\Capstone Code\\Satisfaction Data.csv");
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\n");
        sc.next();  // skip the header
        int satisfied = 0; int dissatified = 0;
        int loyal = 0; int disloyal = 0; //"Loyal Customer", "disloyal Customer"
        int businessT = 0; int personal = 0; //"Business travel", "Personal Travel"
        int businessC = 0; int eco = 0; int otherC = 0; //"Buisness", "Eco", "Other"
        int dDS = 0; int aDS = 0; int dDD = 0; int aDD = 0;
        int i = 1; // Line

        while (sc.hasNext())
        {
            i += 1;
            String r = sc.next();
            String[] re = r.split(",");
            String satis = re[0];
            String gender = re[1];
            String custType = re[2];
            int age = Integer.parseInt(re[3]);
            String travelType = re[4];
            String clas = re[5];
            int distance = Integer.parseInt(re[6]);
            int food = Integer.parseInt(re[7]);
            int wifi = Integer.parseInt(re[8]);
            int cleanliness = Integer.parseInt(re[9]);
            int depDelay = Integer.parseInt(re[10]);
            int arrDelay = 0;
            if (!re[11].trim().isEmpty()) { arrDelay = Integer.parseInt(re[11].trim()); }

            Review review = new Review(satis,gender,custType,age,travelType,clas,distance,food,wifi,cleanliness,depDelay,arrDelay);
            Review.addReview(review);

            // Get data
            List<Integer> ageDist = List.of(age, distance);
            Descriptive.addRDict(ageDist, i);
            total += 1;
            if (satis.equals("satisfied"))
            { satisfied += 1; dDS += depDelay; aDS += arrDelay; }
            else if (satis.equals("dissatisfied"))
            { dissatified += 1; dDD += depDelay; aDD += arrDelay; Review.addDissatisfiedReview(review);}
            if (custType.equals("Loyal Customer")) { loyal += 1; }
            else if (custType.equals("disloyal Customer")) { disloyal += 1; }
            if (travelType.equals("Business travel")) { businessT += 1; }
            else if (travelType.equals("Personal Travel")) { personal += 1; }
            if (clas.equals("Business")) { businessC += 1; }
            else if (clas.equals("Eco")) { eco += 1; }
            else {otherC += 1;}

        }

        // Set the data lists
        satisfaction.add(0, (float)satisfied); satisfaction.add(1, (float)dissatified);
        typeofTravel.add(0, (float)businessT); typeofTravel.add(1, (float)personal);
        custType.add(0, (float)loyal); custType.add(1, (float)disloyal);
        classs.add(0, (float)businessC); classs.add(1, (float)eco); classs.add(2, (float)otherC);

        // Set the graph visibilities
        BarGraphCB.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
        {
            if (newSelection != null)
            {
                if (newSelection.equals("Average Delay"))
                {
                    BarChart.setVisible(true);
                    ScatterChart.setVisible(false);
                }
                else if (newSelection.equals("Age vs Flight Distance"))
                {
                    BarChart.setVisible(false);
                    ScatterChart.setVisible(true);
                }
            }
        });

        // Set the scatter plot data
        Descriptive.reverseDict();
        XYChart.Series set1 = new XYChart.Series<>();
        set1 = Descriptive.getScatterDat(set1);
        ScatterChart.getData().add(set1);
        for (XYChart.Series<String, Integer> series : BarChart.getData()) {
            for(XYChart.Data data : series.getData())
            {
                data.getNode().setStyle("-fx-color: crimson;");
            }
        }

        // Set the bar chart data and colors
        dDS = (int) (dDS / satisfaction.get(0));
        aDS = (int) (aDS / satisfaction.get(0));
        dDD = (int) (dDD / satisfaction.get(1));
        aDD = (int) (aDD / satisfaction.get(1));

        XYChart.Series series1 = new XYChart.Series<>();
        series1.setName("Satisfied");
        series1.getData().add(new XYChart.Data("Departure Delay", dDS));
        series1.getData().add(new XYChart.Data("Arrival Delay", aDS));
        XYChart.Series series2 = new XYChart.Series<>();
        series2.setName("Dissatisfied");
        series2.getData().add(new XYChart.Data("Departure Delay", dDD));
        series2.getData().add(new XYChart.Data("Arrival Delay", aDD));
        BarChart.getData().addAll(series1,series2);

        for (XYChart.Series<String, Integer> series : BarChart.getData()) {
            if(series.getName().equals("Satisfied"))
            {
                for(XYChart.Data data : series.getData())
                {
                    data.getNode().setStyle("-fx-bar-fill: crimson;");
                }
            }
            if(series.getName().equals("Dissatisfied"))
            {
                for(XYChart.Data data : series.getData())
                {
                    data.getNode().setStyle("-fx-bar-fill: pink;");
                }
            }
        }

        for(Node node : BarChart.lookupAll("Label.chart-legend-item"))
        {
            Label tempLabel = (Label)node;
            if(tempLabel.getText().equals("Satisfied"))
            {
                tempLabel.getGraphic().setStyle("-fx-bar-fill: crimson;");
            }
            if(tempLabel.getText().equals("Dissatisfied"))
            {
                tempLabel.getGraphic().setStyle("-fx-bar-fill: pink;");
            }
        }

        // Set the pie charts
        pieChartCalculations();
        custTypeData.add(new PieChart.Data("Loyal", custType.get(0)));
        custTypeData.add(new PieChart.Data("Disloyal", custType.get(1)));
        CustTypePC.setData(custTypeData);
        travelData.add(new PieChart.Data("Business", typeofTravel.get(0)));
        travelData.add(new PieChart.Data("Personal", typeofTravel.get(1)));
        TravelPC.setData(travelData);
        classData.add(new PieChart.Data("Business", classs.get(0)));
        classData.add(new PieChart.Data("Economy", classs.get(1)));
        classData.add(new PieChart.Data("Other", classs.get(2)));
        ClassPC.setData(classData);

        // Change pie chart colors
        custTypeData.get(0).getNode().setStyle("-fx-pie-color: coral;");
        custTypeData.get(1).getNode().setStyle("-fx-pie-color: pink;");
        travelData.get(0).getNode().setStyle("-fx-pie-color: coral;");
        travelData.get(1).getNode().setStyle("-fx-pie-color: pink;");
        classData.get(0).getNode().setStyle("-fx-pie-color: coral;");
        classData.get(1).getNode().setStyle("-fx-pie-color: pink;");
        classData.get(2).getNode().setStyle("-fx-pie-color: crimson;");

        // Set data for the combo box
        BarGraphCB.setItems(graphOptions);
        BarGraphCB.setValue(graphOptions.get(0));

        // Set the text for the display box
        String displayText = " " + LocalDate.now() + ":\n\n " + satisfaction.get(0).toString() + "% of the customers\n are satisfied." +
                "\n That is not enough!\n\n Click on the view\n improvements button to\n determine what needs to be\n improved." +
                "\n\n Click on the predict\n satisfaction button to\n predict the satisfaction of a\n customer from their\n attributes." +
                "\n\n Click on the review sorter to\n view the results based on\n there attributes.";
        DisplayLabel.setText(displayText);

    }

    /** This method calculates the percentages for the pie charts. */
    public void pieChartCalculations()
    {
        // Calculate the satisfaction percentages
        float satP = (satisfaction.get(0) * 100)/ (float) total;
        float dissatP = (satisfaction.get(1) * 100)/total;
        satisfaction.set(0,satP); satisfaction.set(1,dissatP);

        // Calculate the type of travel percentages
        float btP = (typeofTravel.get(0) * 100)/ (float) total;
        float pP = (typeofTravel.get(1) * 100)/total;
        typeofTravel.set(0, btP); typeofTravel.set(1, pP);

        // Calculate the customer type percentages
        float lP = (custType.get(0) * 100)/ (float) total;
        float disLP = (custType.get(1) * 100)/total;
        custType.set(0, lP); custType.set(1, disLP);

        // Calculate the class percentages
        float bcP = (classs.get(0) * 100)/ (float) total;
        float eP = (classs.get(1) * 100)/total;
        float oP = (classs.get(2) * 100)/total;
        classs.set(0, bcP); classs.set(1, eP); classs.set(2, oP);
    }

    public void logout(MouseEvent mouseEvent) throws IOException
    {
        // Load the login screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void viewImprovements(MouseEvent mouseEvent) throws IOException
    {
        // Load the improvements screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ImprovementsScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void predictSatisfaction(MouseEvent mouseEvent) throws IOException
    {
        // Load the prediction screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/PredictiveScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void reviewSorter(MouseEvent mouseEvent) throws IOException
    {
        // Load the review sorter screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ReveiwSorterScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
