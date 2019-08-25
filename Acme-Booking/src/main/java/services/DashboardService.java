package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.DashboardRepository;
import domain.Category;

@Transactional
@Service
public class DashboardService {
	
	// Managed repository ------------------------------
	
	@Autowired
	private DashboardRepository dashboardRepository;
	
	// Other business methods -------------------------------
	
	public Double[] statsBookingsPerRoom() {
		return this.dashboardRepository.statsBookingsPerRoom();
	}
	
	public Double[] statsServicesPerRoom() {
		return this.dashboardRepository.statsServicesPerRoom();
	}
	
	public Double[] statsPricePerRoom() {
		return this.dashboardRepository.statsPricePerRoom();
	}
	
	public Double ratioRevisionPendingByFinalRooms() {
		return this.dashboardRepository.ratioRevisionPendingByFinalRooms();
	}
	
	public Double ratioAcceptedByFinalRooms() {
		return this.dashboardRepository.ratioAcceptedByFinalRooms();
	}
	
	public Double ratioRejectedByFinalRooms() {
		return this.dashboardRepository.ratioRejectedByFinalRooms();
	}
	
	public Double ratioRoomsOutOfService() {
		return this.dashboardRepository.ratioRoomsOutOfService();
	}
	
	public Category topCategoryByRooms() {
		return this.dashboardRepository.topCategoryByRooms().get(0);
	}
	
	public Double[] statsFinder() {
		return this.dashboardRepository.statsFinder();
	}
	
	public Double ratioFindersEmpty() {
		return this.dashboardRepository.ratioFindersEmpty();
	}
	
}
