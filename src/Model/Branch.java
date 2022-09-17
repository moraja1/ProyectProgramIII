package Model;

import java.util.HashMap;
import java.util.Set;

public class Branch {
    private String id;
    private String address;
    private Double zoning_percentage;
    private Coordinates coords;
    private HashMap<String, Employee> employees;

    public Branch(){

    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getZoning_percentage() {
        return zoning_percentage;
    }

    public void setZoning_percentage(Double zoning_percentage) {
        this.zoning_percentage = zoning_percentage;
    }

    public Coordinates getCoords() {
        return coords;
    }

    public void setCoords(Coordinates coords) {
        this.coords = coords;
    }

    public HashMap<String, Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(HashMap<String, Employee> employees) {
        this.employees = employees;
    }
}
