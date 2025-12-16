package tech.erben.java17.records.solution.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record SalesConsultant(
        String name,
        String email,
        List<Customer> assignedCustomers
) {

    public SalesConsultant {
        Objects.requireNonNull(name, "name darf nicht null sein");
        Objects.requireNonNull(email, "email darf nicht null sein");
        assignedCustomers = List.copyOf(assignedCustomers == null ? List.of() : assignedCustomers);
    }

    public SalesConsultant withAssignedCustomers(List<Customer> customers) {
        Objects.requireNonNull(customers, "customers darf nicht null sein");
        return new SalesConsultant(name, email, customers);
    }

    public SalesConsultant withAdditionalCustomer(Customer customer) {
        Objects.requireNonNull(customer, "customer darf nicht null sein");
        var updated = new ArrayList<>(assignedCustomers);
        updated.add(customer);
        return new SalesConsultant(name, email, updated);
    }
}
