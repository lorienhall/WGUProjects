package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Review
{
    /** Various attributes and lists. */
    private static ObservableList<String> obSatisfaction = FXCollections.observableArrayList(" ", "satisfied", "dissatisfied");
    private static ObservableList<String> obGender = FXCollections.observableArrayList(" ", "Female", "Male");
    private static ObservableList<String> obCustType = FXCollections.observableArrayList(" ", "Loyal Customer", "disloyal Customer");
    private static ObservableList<String> obtravelType = FXCollections.observableArrayList(" ", "Business travel", "Personal Travel");
    private static ObservableList<String> obClass = FXCollections.observableArrayList(" ", "Buisness", "Eco", "Other");
    private static List<Review> allReviews = new ArrayList<>();
    private static List<Review> allDissatisfiedReviews = new ArrayList<>();
    private String satis;
    private String gender;
    private String custType;
    private int age;
    private String travelType;
    private String clas;
    private int distance;
    private int food;
    private int wifi;
    private int cleanliness;
    private int depDelay;
    private int arrDelay;


    /** Accounts constructor. */
    public Review(String satis, String gender, String custType, int age, String travelType, String clas, int distance,
                  int food, int wifi, int cleanliness, int depDelay, int arrDelay)
    {
        this.satis = satis;
        this.gender = gender;
        this.custType = custType;
        this.age = age;
        this.travelType = travelType;
        this.clas = clas;
        this.distance = distance;
        this.food = food;
        this.wifi = wifi;
        this.cleanliness = cleanliness;
        this.depDelay = depDelay;
        this.arrDelay = arrDelay;
    }

    /** Review sorter. */
    public static ObservableList<Review> reviewSorter(String satisf, String gend, String cType, int agee, String tType, String cla, int fDistance)
    {
        ObservableList<Review> sortedReviews = FXCollections.observableArrayList();
        Iterator<Review> iterator = allReviews.iterator();
        int dc = determineCriteria(satisf, gend, cType, agee, tType, cla, fDistance);
        while (iterator.hasNext())
        {
            Review r = iterator.next();
            switch (dc)
            {
                case 0:
                    break;
                case 1:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getCustType().equals(cType)
                            & r.getAge() == agee & r.getTravelType().equals(tType) & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 2:
                    if (r.getGender().equals(gend) & r.getCustType().equals(cType) & r.getAge() == agee
                            & r.getTravelType().equals(tType) & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 3:
                    if (r.getSatis().equals(satisf) & r.getCustType().equals(cType) & r.getAge() == agee
                            & r.getTravelType().equals(tType) & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 4:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getAge() == agee
                            & r.getTravelType().equals(tType) & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 5:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getCustType().equals(cType)
                            & r.getTravelType().equals(tType) & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 6:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getCustType().equals(cType)
                            & r.getAge() == agee & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 7:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getCustType().equals(cType)
                            & r.getAge() == agee & r.getTravelType().equals(tType) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 8:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getCustType().equals(cType)
                            & r.getAge() == agee & r.getTravelType().equals(tType) & r.getClas().equals(cla))
                    { sortedReviews.add(r); }
                    break;
                case 9:
                    if (r.getCustType().equals(cType) & r.getAge() == agee & r.getTravelType().equals(tType)
                            & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 10:
                    if (r.getGender().equals(gend) & r.getAge() == agee & r.getTravelType().equals(tType)
                            & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 11:
                    if (r.getGender().equals(gend) & r.getCustType().equals(cType) & r.getTravelType().equals(tType)
                            & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 12:
                    if (r.getGender().equals(gend) & r.getCustType().equals(cType) & r.getAge() == agee
                            & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 13:
                    if (r.getGender().equals(gend) & r.getCustType().equals(cType) & r.getAge() == agee
                            & r.getTravelType().equals(tType) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 14:
                    if (r.getGender().equals(gend) & r.getCustType().equals(cType) & r.getAge() == agee
                            & r.getTravelType().equals(tType) & r.getClas().equals(cla))
                    { sortedReviews.add(r); }
                    break;
                case 16:
                    if (r.getSatis().equals(satisf) & r.getAge() == agee & r.getTravelType().equals(tType)
                            & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 17:
                    if (r.getSatis().equals(satisf) & r.getCustType().equals(cType) & r.getTravelType().equals(tType)
                            & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 18:
                    if (r.getSatis().equals(satisf) & r.getCustType().equals(cType) & r.getAge() == agee
                            & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 19:
                    if (r.getSatis().equals(satisf) & r.getCustType().equals(cType) & r.getAge() == agee
                            & r.getTravelType().equals(tType) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 20:
                    if (r.getSatis().equals(satisf) & r.getCustType().equals(cType) & r.getAge() == agee
                            & r.getTravelType().equals(tType) & r.getClas().equals(cla))
                    { sortedReviews.add(r); }
                    break;
                case 22:
                    if (r.getSatis().equals(satisf) & r.getAge() == agee & r.getTravelType().equals(tType)
                            & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 23:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getTravelType().equals(tType)
                            & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 24:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getAge() == agee
                            & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 25:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getAge() == agee
                            & r.getTravelType().equals(tType) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 26:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getAge() == agee
                            & r.getTravelType().equals(tType)& r.getClas().equals(cla))
                    { sortedReviews.add(r); }
                    break;
                case 28:
                    if (r.getSatis().equals(satisf) & r.getCustType().equals(cType) & r.getTravelType().equals(tType)
                            & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 29:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getTravelType().equals(tType)
                            & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 30:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getCustType().equals(cType)
                            & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 31:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getCustType().equals(cType)
                            & r.getTravelType().equals(tType) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 32:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getCustType().equals(cType)
                            & r.getTravelType().equals(tType) & r.getClas().equals(cla))
                    { sortedReviews.add(r); }
                    break;
                case 34:
                    if (r.getSatis().equals(satisf) & r.getCustType().equals(cType) & r.getAge() == agee
                            & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 35:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getAge() == agee
                            & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 36:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getCustType().equals(cType)
                            & r.getClas().equals(cla) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 37:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getCustType().equals(cType)
                            & r.getAge() == agee & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 38:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getCustType().equals(cType)
                            & r.getAge() == agee & r.getClas().equals(cla))
                    { sortedReviews.add(r); }
                    break;
                case 40:
                    if (r.getSatis().equals(satisf) & r.getCustType().equals(cType) & r.getAge() == agee
                            & r.getTravelType().equals(tType) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 41:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getAge() == agee
                            & r.getTravelType().equals(tType) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 42:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getCustType().equals(cType)
                            & r.getTravelType().equals(tType) & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 43:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getCustType().equals(cType)
                            & r.getAge() == agee & r.getDistance() == fDistance)
                    { sortedReviews.add(r); }
                    break;
                case 44:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getCustType().equals(cType)
                            & r.getAge() == agee & r.getTravelType().equals(tType))
                    { sortedReviews.add(r); }
                    break;
                case 46:
                    if (r.getSatis().equals(satisf) & r.getCustType().equals(cType) & r.getAge() == agee
                            & r.getTravelType().equals(tType) & r.getClas().equals(cla))
                    { sortedReviews.add(r); }
                    break;
                case 47:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getAge() == agee
                            & r.getTravelType().equals(tType) & r.getClas().equals(cla))
                    { sortedReviews.add(r); }
                    break;
                case 48:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getCustType().equals(cType)
                            & r.getTravelType().equals(tType) & r.getClas().equals(cla))
                    { sortedReviews.add(r); }
                    break;
                case 49:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getCustType().equals(cType)
                            & r.getAge() == agee & r.getClas().equals(cla))
                    { sortedReviews.add(r); }
                    break;
                case 50:
                    if (r.getSatis().equals(satisf) & r.getGender().equals(gend) & r.getCustType().equals(cType)
                            & r.getAge() == agee & r.getTravelType().equals(tType))
                    { sortedReviews.add(r); }
                    break;

            }
        }
        return sortedReviews;
    }

    /** This method determines which search criteria are not null and should be used to search with. */
    private static int determineCriteria(String satisf, String gend, String cType, int agee, String tType, String cla, int fDistance)
    {
        List<String> criteria = new ArrayList<>();
        // Add the parameters to the list
        if (satisf != null) { criteria.add("satisf"); }
        if (gend != null) { criteria.add("gend"); }
        if (cType != null) { criteria.add("cType"); }
        if (agee != 0) { criteria.add("agee"); }
        if (tType != null) { criteria.add("tType"); }
        if (cla != null) { criteria.add("cla"); }
        if (fDistance != 0) { criteria.add("fDistance"); }

        // Determine all the combinations
        if (criteria.size() == 7)
        {
            return 1;
        }
        else if (criteria.size() == 6)
        {
            if (!criteria.contains("satisf"))
            {
                return 2;
            }
            else if (!criteria.contains("gend"))
            {
                return 3;
            }
            else if (!criteria.contains("cType"))
            {
                return 4;
            }
            else if (!criteria.contains("agee"))
            {
                return 5;
            }
            else if (!criteria.contains("tType"))
            {
                return 6;
            }
            else if (!criteria.contains("cla"))
            {
                return 7;
            }
            else if (!criteria.contains("fDistance"))
            {
                return 8;
            }

        }
        else if (criteria.size() == 5)
        {
            if (!criteria.contains("satisf"))
            {
               if (!criteria.contains("gend"))
                {
                    return 9;
                }
                else if (!criteria.contains("cType"))
                {
                    return 10;
                }
                else if (!criteria.contains("agee"))
                {
                    return 11;
                }
                else if (!criteria.contains("tType"))
                {
                    return 12;
                }
                else if (!criteria.contains("cla"))
                {
                    return 13;
                }
                else if (!criteria.contains("fDistance"))
                {
                    return 14;
                }
            }
            else if (!criteria.contains("gend"))
            {
                if (!criteria.contains("satisf"))
                {
                    return 9;
                }
                else if (!criteria.contains("cType"))
                {
                    return 16;
                }
                else if (!criteria.contains("agee"))
                {
                    return 17;
                }
                else if (!criteria.contains("tType"))
                {
                    return 18;
                }
                else if (!criteria.contains("cla"))
                {
                    return 19;
                }
                else if (!criteria.contains("fDistance"))
                {
                    return 20;
                }
            }
            else if (!criteria.contains("cType"))
            {
                if (!criteria.contains("satisf"))
                {
                    return 10;
                }
                else if (!criteria.contains("gend"))
                {
                    return 22;
                }
                else if (!criteria.contains("agee"))
                {
                    return 23;
                }
                else if (!criteria.contains("tType"))
                {
                    return 24;
                }
                else if (!criteria.contains("cla"))
                {
                    return 25;
                }
                else if (!criteria.contains("fDistance"))
                {
                    return 26;
                }
            }
            else if (!criteria.contains("agee"))
            {
                if (!criteria.contains("satisf"))
                {
                    return 11;
                }
                else if (!criteria.contains("gend"))
                {
                    return 28;
                }
                else if (!criteria.contains("cType"))
                {
                    return 29;
                }
                else if (!criteria.contains("tType"))
                {
                    return 30;
                }
                else if (!criteria.contains("cla"))
                {
                    return 31;
                }
                else if (!criteria.contains("fDistance"))
                {
                    return 32;
                }
            }
            else if (!criteria.contains("tType"))
            {
                if (!criteria.contains("satisf"))
                {
                    return 12;
                }
                else if (!criteria.contains("gend"))
                {
                    return 34;
                }
                else if (!criteria.contains("cType"))
                {
                    return 35;
                }
                else if (!criteria.contains("agee"))
                {
                    return 36;
                }
                else if (!criteria.contains("cla"))
                {
                    return 37;
                }
                else if (!criteria.contains("fDistance"))
                {
                    return 38;
                }
            }
            else if (!criteria.contains("cla"))
            {
                if (!criteria.contains("satisf"))
                {
                    return 13;
                }
                else if (!criteria.contains("gend"))
                {
                    return 40;
                }
                else if (!criteria.contains("cType"))
                {
                    return 41;
                }
                else if (!criteria.contains("agee"))
                {
                    return 42;
                }
                else if (!criteria.contains("tType"))
                {
                    return 43;
                }
                else if (!criteria.contains("fDistance"))
                {
                    return 44;
                }
            }
            else if (!criteria.contains("fDistance"))
            {
                if (!criteria.contains("satisf"))
                {
                    return 14;
                }
                else if (!criteria.contains("gend"))
                {
                    return 46;
                }
                else if (!criteria.contains("cType"))
                {
                    return 47;
                }
                else if (!criteria.contains("agee"))
                {
                    return 48;
                }
                else if (!criteria.contains("tType"))
                {
                    return 49;
                }
                else if (!criteria.contains("cla"))
                {
                    return 50;
                }
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cannot Search");
            alert.setHeaderText("Not Enough Criteria!");
            if (criteria.size() == 4)
            {
                alert.setContentText("There must be at least 5 search characteristics. Please enter one more characteristic.");
            }
            else if (criteria.size() == 3)
            {
                alert.setContentText("There must be at least 5 search characteristics. Please enter two more characteristics.");
            }
            else if (criteria.size() == 2)
            {
                alert.setContentText("There must be at least 5 search characteristics. Please enter three more characteristics.");
            }
            else if (criteria.size() == 1)
            {
                alert.setContentText("There must be at least 5 search characteristics. Please enter four more characteristics.");
            }
            else if (criteria.size() == 0)
            {
                alert.setContentText("There must be at least 5 search characteristics. Please enter characteristics.");
            }
            alert.showAndWait();
            return 0;
        }

        return 0;
    }

    /** Getters */
    public static void addReview(Review review) { allReviews.add(review); }
    public static void addDissatisfiedReview(Review review) { allDissatisfiedReviews.add(review); }
    public static List<Review> getAllDissatisfiedReviews() { return allDissatisfiedReviews; }
    public static List<Review> getAllReviews() { return allReviews; }
    public static ObservableList<String> getObSatisfaction() { return obSatisfaction; }
    public static ObservableList<String> getObGender() { return obGender; }
    public static ObservableList<String> getObCustType() { return obCustType; }
    public static ObservableList<String> getObTravelType() { return obtravelType; }
    public static ObservableList<String> getObClass() { return obClass; }

    public String getSatis() { return satis; }
    public String getGender() { return gender; }
    public String getCustType() { return custType; }
    public int getAge() { return age; }
    public String getTravelType() { return travelType; }
    public String getClas() { return clas; }
    public int getDistance() { return distance; }
    public int getFood() { return food; }
    public int getWifi() { return wifi; }
    public int getCleanliness() { return cleanliness; }
    public int getDepDelay() { return depDelay; }
    public int getArrDelay() { return arrDelay; }
}
