package tech.erben.java17.records.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Klassische POJO-Implementierung mit veränderlichen Feldern, die später auf Records umgestellt werden soll.
 */
public class Car {

    private String vin;
    private String brand;
    private String model;
    private LocalDate firstRegistration;
    private BigDecimal price;
    private String energyType;
    private SalesConsultant consultant;
    private final List<String> notes = new ArrayList<>();

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public LocalDate getFirstRegistration() {
        return firstRegistration;
    }

    public void setFirstRegistration(LocalDate firstRegistration) {
        this.firstRegistration = firstRegistration;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getEnergyType() {
        return energyType;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType;
    }

    public SalesConsultant getConsultant() {
        return consultant;
    }

    public void setConsultant(SalesConsultant consultant) {
        this.consultant = consultant;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void addNote(String note) {
        this.notes.add(note);
    }

    public String getLabel() {
        if (brand == null || model == null || firstRegistration == null) {
            return "Unvollständige Fahrzeugdaten";
        }
        return brand + " " + model + " (" + firstRegistration.getYear() + ")";
    }
}
