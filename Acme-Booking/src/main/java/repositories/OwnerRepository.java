package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {

	@Query("select o from Owner o where o.userAccount.username = ?1")
	Owner findByUsername(String username);
}
