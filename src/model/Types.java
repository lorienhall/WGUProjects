package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This is the class for the appointment types. */
public class Types
{
    /** Attribute declarations. */
    private static ObservableList<Types> TypesByMonth = FXCollections.observableArrayList();
    private String types;
    private Integer numtypes;

    /** Types constructor. */
    public Types (String types, int numtypes)
    {
        this.types = types;
        this.numtypes = numtypes;
    }

    /** This method adds a type. */
    public static void addType(Types types) { TypesByMonth.add(types); }
    /** This method gets all the types. */
    public static ObservableList <Types> getTypesByMonth() { return TypesByMonth;}
    /** This method resets the list. */
    public static void resetTypesByMonth () { TypesByMonth.clear(); }

    /** Getters and setters for the attributes. */
    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public Integer getNumtypes() {
        return numtypes;
    }

    public void setNumtypes(Integer numtypes) {
        this.numtypes = numtypes;
    }
}
