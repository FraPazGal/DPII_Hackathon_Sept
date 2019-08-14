package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
	
//	@Query("select i from IRobot i where i.isDecomissioned = false and i.isDeleted = false")
//	Collection<Room> findIRobotsNotDecomissioned();
//	
//	@Query("select i from IRobot i where i.isDecomissioned = true and i.isDeleted = false and i.scientist.id = ?1")
//	Collection<Room> findIRobotsDecomissionedAndMine(Integer scientistId);
//	
//	@Query("select i from IRobot i where i.isDecomissioned = false and i.isDeleted = false and i.scientist.id = ?1")
//	Collection<Room> findIRobotsNotDecomissionedAndMine(Integer scientistId);
//	
//	@Query("select i from IRobot i where i.scientist.id = ?1")
//	Collection<Room> findIRobotsMine(Integer scientistId);
//	
//	@Query("select i from IRobot i where i.isDeleted = false and i.scientist.id = ?1")
//	Collection<Room> findIRobotsNotDeleted(Integer scientistId);
//	
//	@Query("select case when (count(i) = 0) then true else false end from IRobot i where i.ticker = ?1")
//	boolean uniqueTicket(String ticker);
}
