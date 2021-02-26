package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Review;

import java.io.IOException;

public class ReviewSorterScreen
{
    /** Setting the fx id references for the buttons. */
    public javafx.scene.control.Button EnterB1;
    public javafx.scene.control.Button BackB1;

    /** Setting the fx id references for the combo boxes. */
    @FXML
    private ComboBox<String> SatisfactionCB;
    @FXML
    private ComboBox<String> GenderCB;
    @FXML
    private ComboBox<String> CustTypeCB;
    @FXML
    private ComboBox<String> TravelTypeCB;
    @FXML
    private ComboBox<String> ClassCB;

    /** Setting the fx id references for the label and text fields. */
    public javafx.scene.control.TextField AgeTF1;
    public javafx.scene.control.Label NReviewsL;
    public javafx.scene.control.TextField FDistanceTF;

    /** Setting the fx id references for the tableview. */
    @FXML
    private TableView<Review> ReviewTV;
    @FXML
    private TableColumn<Review, String> SatisfactionTC;
    @FXML
    private TableColumn<Review, String> GenderTC;
    @FXML
    private TableColumn<Review, String> CustTypeTC;
    @FXML
    private TableColumn<Review, Integer> AgeTC;
    @FXML
    private TableColumn<Review, String> TravelTypeTC;
    @FXML
    private TableColumn<Review, Integer> ClassTC;
    @FXML
    private TableColumn<Review, Integer> DistanceTC;
    @FXML
    private TableColumn<Review, Integer> FoodDrinkTC;
    @FXML
    private TableColumn<Review, Integer> DepDelayTC;
    @FXML
    private TableColumn<Review, Integer> ArrDelayTC;


    Stage stage;
    Parent scene;

    public void initialize()
    {
        // Set data for the combo boxes
        SatisfactionCB.setItems(Review.getObSatisfaction());
        GenderCB.setItems(Review.getObGender());
        CustTypeCB.setItems(Review.getObCustType());
        TravelTypeCB.setItems(Review.getObTravelType());
        ClassCB.setItems(Review.getObClass());

        // Prepare the reviews table
        SatisfactionTC.setCellValueFactory(new PropertyValueFactory<>("satis"));
        GenderTC.setCellValueFactory(new PropertyValueFactory<>("gender"));
        CustTypeTC.setCellValueFactory(new PropertyValueFactory<>("custType"));
        AgeTC.setCellValueFactory(new PropertyValueFactory<>("age"));
        TravelTypeTC.setCellValueFactory(new PropertyValueFactory<>("travelType"));
        ClassTC.setCellValueFactory(new PropertyValueFactory<>("clas"));
        DistanceTC.setCellValueFactory(new PropertyValueFactory<>("distance"));
        FoodDrinkTC.setCellValueFactory(new PropertyValueFactory<>("food"));
        DepDelayTC.setCellValueFactory(new PropertyValueFactory<>("depDelay"));
        ArrDelayTC.setCellValueFactory(new PropertyValueFactory<>("arrDelay"));
    }

    public void back(MouseEvent mouseEvent) throws IOException
    {
        // Load the dashboard
        stage = (Stage) ((javafx.scene.control.Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method searches for and displays the reviews that match the entered criteria in the tableview. */
    public void enter(MouseEvent mouseEvent)
    {
        String satisf = null;
        String gendr = null;
        String ctype = null;
        int agee = 0;
        String ttype = null;
        String cla = null;
        int dist = 0;

        if (SatisfactionCB.getSelectionModel().getSelectedItem() != null)
        {
            if (!SatisfactionCB.getSelectionModel().getSelectedItem().equals(" "))
            { satisf = SatisfactionCB.getSelectionModel().getSelectedItem(); }
        }
        if (GenderCB.getSelectionModel().getSelectedItem() != null)
        {
            if (!GenderCB.getSelectionModel().getSelectedItem().equals(" "))
            { gendr = GenderCB.getSelectionModel().getSelectedItem(); }
        }
        if (CustTypeCB.getSelectionModel().getSelectedItem() != null)
        {
            if (!CustTypeCB.getSelectionModel().getSelectedItem().equals(" "))
            { ctype = CustTypeCB.getSelectionModel().getSelectedItem(); }
        }
        if (!AgeTF1.getText().isEmpty())
        { agee = Integer.parseInt(AgeTF1.getText()); }
        if (TravelTypeCB.getSelectionModel().getSelectedItem() != null)
        {
            if (!TravelTypeCB.getSelectionModel().getSelectedItem().equals(" "))
            { ttype = TravelTypeCB.getSelectionModel().getSelectedItem(); }
        }
        if (ClassCB.getSelectionModel().getSelectedItem() != null)
        {
            if (!ClassCB.getSelectionModel().getSelectedItem().equals(" "))
            { cla = ClassCB.getSelectionModel().getSelectedItem(); }
        }
        if (!FDistanceTF.getText().isEmpty())
        { dist = Integer.parseInt(FDistanceTF.getText()); }

        ObservableList<Review> relevantReviews = Review.reviewSorter(satisf, gendr, ctype, agee, ttype, cla, dist);
        NReviewsL.setText(String.valueOf(relevantReviews.size()));
        ReviewTV.setItems(relevantReviews);
    }
}
