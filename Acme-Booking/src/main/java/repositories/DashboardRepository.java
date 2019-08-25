package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;
import domain.Category;

@Repository
public interface DashboardRepository extends JpaRepository<Administrator, Integer> {
	
	@Query("select max(1.0*(select count(*) from Booking b where b.room = r)),min(1.0*(select count(*) from Booking b where b.room = r)),avg(1.0*(select count(*) from Booking b where b.room = r)),stddev(1.0*(select count(*) from Booking b where b.room = r)) from Room r")
	Double[] statsBookingsPerRoom();
	
	@Query("select max(1.0*(select count(*) from Service s where s.room = r)),min(1.0*(select count(*) from Service s where s.room = r)),avg(1.0*(select count(*) from Service s where s.room = r)),stddev(1.0*(select count(*) from Service s where s.room = r)) from Room r")
	Double[] statsServicesPerRoom();
	
	@Query("select max(r.pricePerHour),min(r.pricePerHour),avg(r.pricePerHour),stddev(r.pricePerHour) from Room r")
	Double[] statsPricePerRoom();
	
	@Query("select (sum(case when r.status = 'REVISION-PENDING' then 1.0 else 0 end)/count(r)) from Room r where r.status != 'DRAFT' and r.status != 'OUT-OF-SERVICE'")
	Double ratioRevisionPendingByFinalRooms();
	
	@Query("select (sum(case when r.status = 'ACTIVE' then 1.0 else 0 end)/count(r)) from Room r where r.status != 'DRAFT' and r.status != 'OUT-OF-SERVICE'")
	Double ratioAcceptedByFinalRooms();
	
	@Query("select (sum(case when r.status = 'REJECTED' then 1.0 else 0 end)/count(r)) from Room r where r.status != 'DRAFT' and r.status != 'OUT-OF-SERVICE'")
	Double ratioRejectedByFinalRooms();
	
	@Query("select (sum(case when r.status = 'OUT-OF-SERVICE' then 1.0 else 0 end)/count(r)) from Room r")
	Double ratioRoomsOutOfService();
	
	@Query("select c from Category c where c.rooms.size = (select max(c.rooms.size) from Category c) order by c.id")
	List<Category> topCategoryByRooms();
	
	@Query("select max(f.results.size), min(f.results.size), avg(f.results.size),stddev(f.results.size) from Finder f")
	Double[] statsFinder();
	
	@Query("select (sum(case when f.results.size=0 then 1.0 else 0 end)/count(f)) from Finder f")
	Double ratioFindersEmpty();

}
