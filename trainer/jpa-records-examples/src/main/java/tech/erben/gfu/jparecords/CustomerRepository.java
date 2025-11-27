package tech.erben.gfu.jparecords;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByFirstNameAndLastName(String firstName, String lastName);

    @Query("""
            select new tech.erben.gfu.jparecords.CustomerSummary(
                concat(c.firstName, ' ', c.lastName),
                c.address.city
            )
            from Customer c
            where c.address.city = :city
            order by c.lastName asc, c.firstName asc
            """)
    List<CustomerSummary> findCustomerSummariesByCity(@Param("city") String city);
}
