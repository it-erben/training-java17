package tech.erben.java17.records.model;

import java.util.ArrayList;
import java.util.List;

public class SalesConsultant {

    private String name;
    private String email;
    private final List<Customer> assignedCustomers = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Customer> getAssignedCustomers() {
        return assignedCustomers;
    }

    public void addCustomer(Customer customer) {
        assignedCustomers.add(customer);
    }
}
