package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Review;

import java.io.IOException;
import java.util.*;

public class ImprovementsScreen
{

    /** Setting the fx id references for the label and buttons. */
    @FXML
    private Button BackB;
    @FXML
    private Button GetSuggestionB;
    public Label SuggestionL;


    Stage stage;
    Parent scene;
    List<Float> food = new ArrayList<>();
    List<Float> typeofTravel = new ArrayList<>();
    List<Float> cleanliness = new ArrayList<>();
    List<Float> classs = new ArrayList<>();

    /** These variables are used to gather and calculate the suggestion data. */
    float zeroF = 0; float oneF = 0; float twoF = 0; float threeF = 0; float fourF = 0; float fiveF = 0; // 0,1,2,3,4,5 food
    float businessT = 0; float personal = 0; //"Business travel", "Personal Travel" travel type
    float businessC = 0; float eco = 0; float otherC = 0; //"Business", "Eco", "Other" class
    float zeroC = 0; float oneC = 0; float twoC = 0; float threeC = 0; float fourC = 0; float fiveC = 0; // 0,1,2,3,4,5 cleanliness

    /** This method determines what should be improved when the get suggestion button is pressed. */
    public void getSuggestion(MouseEvent mouseEvent)
    {
        // Get the composition of the dissatisfied reviews.
        for (Review r : Review.getAllDissatisfiedReviews())
        {
            if (r.getClas().equals("Business"))
            { businessC++; }
            else if (r.getClas().equals("Eco"))
            { eco++; }
            else
            { otherC++; }
            if (r.getTravelType().equals("Business travel"))
            { businessT++; }
            else if (r.getTravelType().equals("Personal Travel"))
            { personal++; }

            if (r.getCleanliness() == 0) { zeroC++; }
            else if (r.getCleanliness() == 1)
            { oneC++; }
            else if (r.getCleanliness() == 2)
            { twoC++; }
            else if (r.getCleanliness() == 3)
            { threeC++; }
            else if (r.getCleanliness() == 4)
            { fourC++; }
            else if (r.getCleanliness() == 5)
            { fiveC++; }

            if (r.getFood() == 0)
            { zeroF++; }
            else if (r.getFood() == 1)
            { oneF++; }
            else if (r.getFood() == 2)
            { twoF++; }
            else if (r.getFood() == 3)
            { threeF++; }
            else if (r.getFood() == 4)
            { fourF++; }
            else if (r.getFood() == 5)
            { fiveF++; }
        }

        // Do the calculations
        float t = Review.getAllDissatisfiedReviews().size();
        zeroF = zeroF / t; oneF = oneF / t; twoF = twoF / t; threeF = threeF / t; fourF = fourF / t; fiveF = fiveF / t;
        zeroC = zeroC / t; oneC = oneC / t; twoC = twoC / t; threeC = threeC / t; fourC = fourC / t; fiveC = fiveC / t;
        businessT = businessT / t; personal = personal / t;
        businessC = businessC / t; eco = eco / t; otherC = otherC / t;

        // Add the gathered data to the lists for comparison
        food.add(0, zeroF); food.add(1, oneF); food.add(2, twoF);
        food.add(3, threeF); food.add(4, fourF); food.add(5, fiveF);
        cleanliness.add(0, zeroC); cleanliness.add(1, oneC); cleanliness.add(2, twoC);
        cleanliness.add(3, threeC); cleanliness.add(4, fourC); cleanliness.add(5, fiveC);
        typeofTravel.add(0, businessT); typeofTravel.add(1, personal);
        classs.add(0, businessC); classs.add(1, eco); classs.add(2, otherC);

        // Determine which percentage is the greatest and store its position
        int fplace = 0;
        int tplace = 0;
        int cleanplace = 0;
        int clplace = 0;
        for (float f : food)
        {
            if (f > food.get(fplace))
            { fplace = food.indexOf(f); }
        }
        for (float ty : typeofTravel)
        {
            if (ty > typeofTravel.get(tplace))
            { tplace = typeofTravel.indexOf(ty); }
        }
        for (float cle : cleanliness)
        {
            if (cle > cleanliness.get(cleanplace))
            { cleanplace = cleanliness.indexOf(cle); }
        }
        for (float cla : classs)
        {
            if (cla > classs.get(clplace))
            { clplace = classs.indexOf(cla); }
        }

        // Put them in a list to find the greatest one
        List<Float> greatest = new ArrayList<>();
        int gplace = 0;
        greatest.add(0, food.get(fplace));
        greatest.add(1, typeofTravel.get(tplace));
        greatest.add(2, cleanliness.get(cleanplace));
        greatest.add(3, classs.get(clplace));

        for (float g : greatest)
        {
            if (g > greatest.get(gplace))
            { gplace = greatest.indexOf(g); }
        }

        // Set the label to display the thing to improve
        if (gplace == 0)
        { SuggestionL.setText("Food and Drink Service"); }
        else if (gplace == 1)
        {
            if (tplace == 0)
            { SuggestionL.setText("Business Travel"); }
            if (tplace == 1)
            { SuggestionL.setText("Personal Travel"); }
        }
        else if (gplace == 2)
        { SuggestionL.setText("Aircraft Cleanliness"); }
        else if (gplace == 3)
        {
            if (clplace == 0)
            { SuggestionL.setText("Business Class"); }
            if (clplace == 1)
            { SuggestionL.setText("Economy Class"); }
            if (clplace == 2)
            { SuggestionL.setText("Other Classes"); }
        }

    }

    public void back(MouseEvent mouseEvent) throws IOException
    {
        // Load the dashboard
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


}
