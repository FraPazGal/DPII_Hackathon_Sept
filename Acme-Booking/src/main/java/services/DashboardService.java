package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.DashboardRepository;

@Transactional
@Service
public class DashboardService {
	
	// Managed repository ------------------------------
	
	@Autowired
	private DashboardRepository dashboardRepository;
	
	// Other business methods -------------------------------
	
}
