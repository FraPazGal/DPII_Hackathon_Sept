package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

//	@Query("select distinct i from IRobot i where ( i.title like %?1% or i.ticker like %?1% or i.description like %?1%) and  i.price <= ?2 and i.price >= ?3 and i.isDecomissioned = false")
//	Collection<Room> search(String keyWord, Double maximumPrice, Double minimumPrice);
//	
//	@Query("select distinct i from IRobot i where ( i.title like %?1% or i.ticker like %?1% or i.description like %?1%) and i.isDecomissioned = false")
//	Collection<Room> searchAnon(String keyWord);
//
//	@Query("select i from IRobot i where i.isDecomissioned = false")
//	Collection<Room> allIRobotsNotDecomissioned();
//	
	@Query("select f from Finder f where f.customer.id = ?1")
	Finder findFinderByCustomerId(int customerId);
	
}
