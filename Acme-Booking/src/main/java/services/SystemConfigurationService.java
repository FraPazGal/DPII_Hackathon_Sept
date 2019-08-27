
package services;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SystemConfigurationRepository;
import domain.SystemConfiguration;

@Service
@Transactional
public class SystemConfigurationService {

	@Autowired
	private SystemConfigurationRepository	systemConfigurationRepository;

	@Autowired
	private UtilityService					utilityService;

	@Autowired
	private Validator						validator;


	public SystemConfiguration findMySystemConfiguration() {
		return this.systemConfigurationRepository.getMySystemConfiguration();
	}

	public SystemConfiguration save(final SystemConfiguration systemConfiguration) {

		Assert.isTrue(this.utilityService.checkAuthority(this.utilityService.findByPrincipal(), "ADMIN"), "not.allowed");

		return this.systemConfigurationRepository.save(systemConfiguration);
	}

	public SystemConfiguration reconstruct(final SystemConfiguration systemConfiguration, final String welcomeES, final String welcomeEN, final BindingResult binding) {

		try {
			Assert.isTrue(!welcomeEN.isEmpty());
			Assert.isTrue(!welcomeES.isEmpty());

		} catch (final Throwable oops) {
			binding.rejectValue("welcomeMessage", "no.both.welcome");
		}
		systemConfiguration.setWelcomeMessage(this.reconstruct(welcomeES, welcomeEN));

		if (!binding.hasErrors())
			this.validator.validate(systemConfiguration, binding);

		return systemConfiguration;
	}

	public Map<String, String> reconstruct(final String stringES, final String stringEN) {
		final Map<String, String> res = new HashMap<>();

		res.put("Español", stringES);
		res.put("English", stringEN);

		return res;
	}

}
