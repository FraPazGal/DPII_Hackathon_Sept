package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
	
	@Query("select r from Room r where r.visibility == 'ACTIVE'")
	Collection<Room> findRoomsForBooking();

	@Query("select r from Room r r.visibility == 'DRAFT' and r.owner.id = ?1")
	Collection<Room> findRoomsDraftAndMine(Integer ownerId);
	
	@Query("select r from Room r r.visibility == 'ACTIVE' and r.owner.id = ?1")
	Collection<Room> findRoomsActiveAndMine(Integer ownerId);
	
	@Query("select r from Room r r.visibility == 'OUTOFSERVICE' and r.owner.id = ?1")
	Collection<Room> findRoomsOutOfServiceAndMine(Integer ownerId);

	@Query("select case when (count(i) = 0) then true else false end from Room r where r.ticker = ?1")
	boolean uniqueTicket(String ticker);
}
