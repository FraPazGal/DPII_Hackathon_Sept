
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

	@Query("select b from Booking b where b.room.owner.id = ?1")
	Collection<Booking> findAllFinal(int id);

	@Query("select b from Booking b where b.customer.id = ?1")
	Collection<Booking> findAllCustomer(int id);

	@Query("select b from Booking b where b.room.id = ?1")
	Collection<Booking> findByRoom(int id);

	@Query("select b from Booking b where b.status='ACCEPTED' and b.reservationDate >=?1 and b.reservationDate <=?2")
	Collection<Booking> findOcupped(Date init, Date end);

	@Query("select b from Booking b where b.customer.id = ?1")
	Collection<Booking> deleteFromCustomerId(Integer customerId);

	@Query("select b from Booking b where b.room.id = ?1 and b.reservationDate >= ?2")
	Collection<Booking> futureBookingsOfRoom(Integer roomId, Date now);

	@Query("select b from Booking b where b.status = 'PENDING' and b.room.id = ?1")
	Collection<Booking> pendingByRoomId(Integer roomId);

	@Query("select b from Booking b where b.status='PENDING' and (b.reservationDate < ?1 or b.room.status='OUT-OF-SERVICE')")
	Collection<Booking> getOld(Date now);
}
