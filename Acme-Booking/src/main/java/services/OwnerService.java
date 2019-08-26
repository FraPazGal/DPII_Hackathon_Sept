package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.OwnerRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Owner;
import forms.ActorForm;
import forms.ActorRegistrationForm;

@Transactional
@Service
public class OwnerService {

	// Managed repository ------------------------------------

	@Autowired
	private OwnerRepository ownerRepository;

	// Supporting services -----------------------------------

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private Validator validator;

	// CRUD Methods ------------------------------------------

	public Owner create() {
		Owner res;

		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Owner();

		auth.setAuthority(Authority.OWNER);
		authority.add(auth);
		userAccount.setAuthorities(authority);
		res.setUserAccount(userAccount);

		return res;
	}

	public Owner findOne(final Integer ownerId) {

		Owner result = this.ownerRepository.findOne(ownerId);
		Assert.notNull(result, "wrong.id");
		return result;
	}

	public List<Owner> findAll() {
		return this.ownerRepository.findAll();
	}

	public Owner save(final Owner owner) {
		Owner res;
		Assert.notNull(owner, "not.allowed");

		if (owner.getId() != 0) {
			Actor principal = this.utilityService.findByPrincipal();
			
			owner.setUserAccount(principal.getUserAccount());
		}
		
		res = this.ownerRepository.save(owner);

		return res;
	}
	
	public void delete(final Owner owner) {
		Actor principal = this.utilityService.findByPrincipal();
		
		Assert.notNull(owner, "not.allowed");
		Assert.isTrue(principal.getId() == owner.getId(), "not.allowed");

		this.ownerRepository.delete(owner);
	}

	// Other business methods -------------------------------
	
	public Owner reconstruct(ActorRegistrationForm form, BindingResult binding) {

		/* Creating owner */
		Owner res = this.create();

		res.setName(form.getName());
		res.setSurname(form.getSurname());
		res.setPhoto(form.getPhoto());
		res.setEmail(form.getEmail());
		res.setPhoneNumber(form.getPhoneNumber());
		res.setAddress(form.getAddress());
		
		if(form.getMiddleName() != null) {
			res.setMiddleName(form.getMiddleName());
		}

		/* Creating user account */
		final UserAccount userAccount = new UserAccount();

		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.OWNER);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		userAccount.setUsername(form.getUsername());

		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		userAccount.setPassword(encoder.encodePassword(form.getPassword(), null));

		res.setUserAccount(userAccount);

		if(!binding.hasErrors()) {
			/* Email */
			if (form.getEmail() != null) {
				try {
					Assert.isTrue(this.utilityService.checkEmail(form.getEmail(), "OWNER"));

				} catch (Throwable oops) {
					binding.rejectValue("email", "email.error");
				}
			}
			
			/* Password confirmation */
			if (form.getPassword() != null) {
				try {
					Assert.isTrue(form.getPassword().equals(form.getPasswordConfirmation()));
				} catch (final Throwable oops) {
					binding.rejectValue("passwordConfirmation", "password.confirmation.error");
				}
			}
			
			/* Username */
			if (form.getUsername() != null) {
				try {
					Assert.isTrue(this.utilityService.existsUsername(form.getUsername()));
				} catch (final Throwable oops) {
					binding.rejectValue("username", "username.error");
				}
			}
			
			/* Terms&Conditions */
			if (form.getTermsAndConditions() != null) {
				try {
					Assert.isTrue((form.getTermsAndConditions()));
				} catch (final Throwable oops) {
					binding.rejectValue("termsAndConditions", "terms.error");
				}
			}

			/* Managing phone number */
			if (form.getPhoneNumber() != null) {
				try {
					final char[] phoneArray = form.getPhoneNumber().toCharArray();
					if ((!form.getPhoneNumber().equals(null) && !form
							.getPhoneNumber().equals("")))
						if (phoneArray[0] != '+'
								&& Character.isDigit(phoneArray[0])) {
							final String cc = this.systemConfigurationService
									.findMySystemConfiguration().getCountryCode();
							form.setPhoneNumber(cc + " " + form.getPhoneNumber());
						}
				} catch (Throwable oops) {
					binding.rejectValue("phoneNumber", "phone.error");
				}
			}
		}
		return res;
	}

	public Owner reconstruct(ActorForm actorEditionForm, BindingResult binding) {

		/* Creating owner */
		Owner res = this.create();
		Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(principal.getId() == actorEditionForm.getId(), "not.allowed");

		res.setId(actorEditionForm.getId());
		res.setVersion(actorEditionForm.getVersion());
		res.setName(actorEditionForm.getName());
		res.setSurname(actorEditionForm.getSurname());
		res.setPhoto(actorEditionForm.getPhoto());
		res.setEmail(actorEditionForm.getEmail());
		res.setPhoneNumber(actorEditionForm.getPhoneNumber());
		res.setAddress(actorEditionForm.getAddress());
		
		if(actorEditionForm.getMiddleName() != null) {
			res.setMiddleName(actorEditionForm.getMiddleName());
		}
		
		this.validator.validate(res, binding);
		
		if(!binding.hasErrors()) {
			/* Email */
			if (actorEditionForm.getEmail() != null) {
				try {
					Assert.isTrue(this.utilityService.checkEmail(actorEditionForm.getEmail(), "OWNER"));

				} catch (Throwable oops) {
					binding.rejectValue("email", "email.error");
				}
			}

			/* Managing phone number */
			if (actorEditionForm.getPhoneNumber() != null) {
				try {
					final char[] phoneArray = actorEditionForm.getPhoneNumber().toCharArray();
					if ((!actorEditionForm.getPhoneNumber().equals(null) && !actorEditionForm
							.getPhoneNumber().equals("")))
						if (phoneArray[0] != '+'
								&& Character.isDigit(phoneArray[0])) {
							final String cc = this.systemConfigurationService
									.findMySystemConfiguration().getCountryCode();
							actorEditionForm.setPhoneNumber(cc + " " + actorEditionForm.getPhoneNumber());
						}
				} catch (Throwable oops) {
					binding.rejectValue("phoneNumber", "phone.error");
				}
			}
		}

		return res;
	}

	public Owner findByUsername(final String username) {
		return this.ownerRepository.findByUsername(username);
	}

	public void flush() {
		this.ownerRepository.flush();
	}
}
