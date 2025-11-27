package tech.erben.gfu.jparecords;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class RecordDemoService {

    private static final Logger log = LoggerFactory.getLogger(RecordDemoService.class);

    private final CustomerRepository repository;
    private final EntityManager entityManager;

    RecordDemoService(CustomerRepository repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Transactional
    void logExamples() {
        if (repository.count() == 0) {
            repository.saveAll(sampleCustomers());
        }

        repository.findAll()
                .forEach(customer -> log.info("Embeddable Record: {} wohnt in {}", customer.fullName(),
                        customer.getAddress().city()));

        List<CustomerSummary> berlinSummaries = repository.findCustomerSummariesByCity("Berlin");
        berlinSummaries.forEach(summary -> log.info("DTO Projection -> {}", summary));

        List<Address> addresses = entityManager.createQuery(
                "select c.address from Customer c", Address.class
        ).getResultList();
        log.info("Record Embeddables aus Query Language: {}", addresses);
    }

    private List<Customer> sampleCustomers() {
        return List.of(
                new Customer("Ada", "Lovelace", new Address("Unter den Linden 1", "10117", "Berlin")),
                new Customer("Grace", "Hopper", new Address("Broadway 12", "10007", "New York")),
                new Customer("Heinz", "Nixdorf", new Address("Am Stadtrand 3", "33100", "Paderborn"))
        );
    }
}
