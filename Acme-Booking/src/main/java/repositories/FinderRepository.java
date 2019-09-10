
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;
import domain.Room;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	//  
	@Query("select distinct r from Room r where ( r.title like %?1% or r.ticker like %?1% or r.description like %?1%) and (r.status='ACTIVE')  and r.pricePerHour <= ?2 and r.capacity >= ?3")
	Collection<Room> search(String keyWord, Double maximumPrice, Integer minimunCapacity);

	@Query("select distinct s.room from Service s where ( s.room.title like %?1% or  s.room.ticker like %?1% or  s.room.description like %?1%) and ( s.room.status='ACTIVE')  and  s.room.pricePerHour <= ?2 and  s.room.capacity >= ?3 and s.name=?4")
	Collection<Room> searchByService(String keyWord, Double maximumPrice, Integer minimunCapacity, String service);

	@Query("select distinct r from Room r where ( r.title like %?1% or r.ticker like %?1% or r.description like %?1%) and (r.status='ACTIVE')")
	Collection<Room> searchAnon(String keyWord);

	@Query("select f from Finder f where f.customer.id = ?1")
	Finder findByCustomer(int id);

	@Query("select f from Finder f where ?1 MEMBER OF f.results ")
	Collection<Finder> findByRoom(int id);
	
	@Query("select f from Finder f where f.customer.id = ?1")
	Finder findFinderByCustomerId(int customerId);
}
