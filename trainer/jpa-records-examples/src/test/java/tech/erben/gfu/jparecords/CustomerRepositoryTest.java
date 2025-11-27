package tech.erben.gfu.jparecords;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        repository.saveAll(List.of(
                new Customer("Ada", "Lovelace", new Address("Unter den Linden 1", "10117", "Berlin")),
                new Customer("Grace", "Hopper", new Address("Broadway 12", "10007", "New York")),
                new Customer("Heinz", "Nixdorf", new Address("Am Stadtrand 3", "33100", "Paderborn"))
        ));
    }

    @Test
    void storesEmbeddableRecord() {
        Customer ada = repository.findByFirstNameAndLastName("Ada", "Lovelace")
                .orElseThrow(() -> new IllegalStateException("Demo-Datensatz fehlt"));

        assertThat(ada.getAddress())
                .isEqualTo(new Address("Unter den Linden 1", "10117", "Berlin"));
    }

    @Test
    void projectsRecordDtoViaQueryLanguage() {
        List<CustomerSummary> berlin = repository.findCustomerSummariesByCity("Berlin");

        assertThat(berlin)
                .containsExactly(new CustomerSummary("Ada Lovelace", "Berlin"));
    }
}
