package model;

/** This is the class for in house parts. */
public class InHouse extends Part{

    private int machineId;

    /** In house constructor. */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
       super(id, name, price, stock, min, max);
       this.machineId = machineId;
    }

    /** Getters and setters for the machine id. */
    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
