
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.DashboardRepository;
import domain.Actor;
import domain.Category;

@Transactional
@Service
public class DashboardService {

	// Managed repository ------------------------------

	@Autowired
	private DashboardRepository	dashboardRepository;

	@Autowired
	private UtilityService		utilityService;


	// Other business methods -------------------------------

	public Double[] statsBookingsPerRoom() {
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"));
		return this.dashboardRepository.statsBookingsPerRoom();
	}

	public Double[] statsServicesPerRoom() {
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"));
		return this.dashboardRepository.statsServicesPerRoom();
	}

	public Double[] statsPricePerRoom() {
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"));
		return this.dashboardRepository.statsPricePerRoom();
	}

	public Double ratioRevisionPendingByFinalRooms() {
		return this.dashboardRepository.ratioRevisionPendingByFinalRooms();
	}

	public Double ratioAcceptedByFinalRooms() {
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"));
		return this.dashboardRepository.ratioAcceptedByFinalRooms();
	}

	public Double ratioRejectedByFinalRooms() {
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"));
		return this.dashboardRepository.ratioRejectedByFinalRooms();
	}

	public Double ratioRoomsOutOfService() {
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"));
		return this.dashboardRepository.ratioRoomsOutOfService();
	}

	public Category topCategoryByRooms() {
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"));
		return this.dashboardRepository.topCategoryByRooms().get(0);
	}

	public Double[] statsFinder() {
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"));
		return this.dashboardRepository.statsFinder();
	}

	public Double ratioFindersEmpty() {
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"));
		return this.dashboardRepository.ratioFindersEmpty();
	}

}
