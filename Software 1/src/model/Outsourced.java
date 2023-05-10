package model;

/** This is the class for outsourced parts. */
public class Outsourced  extends Part{

    private String companyName;

    /** Outsourced constructor. */
    public Outsourced (int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /** Getters and setters for the company name. */
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
