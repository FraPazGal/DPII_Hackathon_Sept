package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
	
	@Query("select r from Room r where r.status = 'ACTIVE'")
	Collection<Room> findRoomsForBooking();
	
	@Query("select r from Room r where r.owner.id = ?1")
	Collection<Room> findRoomsMine(Integer ownerId);

	@Query("select r from Room r where r.status = 'DRAFT' and r.owner.id = ?1")
	Collection<Room> findRoomsDraftAndMine(Integer ownerId);
	
	@Query("select r from Room r where r.status = 'REVISION-PENDING' and (r.owner.id = ?1 or r.administrator.id = ?1)")
	Collection<Room> findRoomsRevisionPendingAndMine(Integer actorId);
	
	@Query("select r from Room r where r.status = 'ACTIVE' and (r.owner.id = ?1 or r.administrator.id = ?1)")
	Collection<Room> findRoomsActiveAndMine(Integer actorId);
	
	@Query("select r from Room r where r.status = 'OUT-OF-SERVICE' and r.owner.id = ?1")
	Collection<Room> findRoomsOutOfServiceAndMine(Integer ownerId);
	
	@Query("select r from Room r where r.status = 'REJECTED' and (r.owner.id = ?1 or r.administrator.id = ?1)")
	Collection<Room> findRoomsRejectedAndMine(Integer actorId);
	
	@Query("select r from Room r where r.status = 'REVISION-PENDING' and r.administrator = null")
	Collection<Room> findRoomsToAssign();
	
	@Query("select case when (count(r) = 0) then true else false end from Room r where r.ticker = ?1")
	boolean uniqueTicket(String ticker);
	
}
