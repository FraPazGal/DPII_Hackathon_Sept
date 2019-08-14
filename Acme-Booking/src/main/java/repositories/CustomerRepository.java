package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Customer;
import domain.Owner;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	@Query("select c from Customer c where c.userAccount.username = ?1")
	Owner findByUsername(String username);

}
