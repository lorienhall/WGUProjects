package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This is the class for Products. */
public class Product {

    /** Attribute declarations. */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /** Product constructor. */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /** The next three methods are for adding, getting, searching, and deleting things from
     * the associated parts list. */
    public void addAssociatedPart (Part part)
    {
        associatedParts.add(part);
    }

    public ObservableList<Part> getAllAssociatedParts()
    {
        return associatedParts;
    }

    public Part lookupAssociatedPart (int i)
    {
        if (!associatedParts.isEmpty())
        {
            for (Part part : associatedParts)
            {
                if (part.getId() == i )
                {
                    return part;
                }
            }
        }
        return null;
    }

    public boolean deleteAssociatedPart (Product product)
    {
        product.associatedParts.removeAll();
        return false;
    }

    /** Getters and setters for the product attributes. */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

}
