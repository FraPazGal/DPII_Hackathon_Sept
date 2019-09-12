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
import domain.Category;
import domain.Owner;
import domain.Room;
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
	private RoomService roomService;
	
	@Autowired
	private ServiceService serviceService;

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
		res.setIsSpammer(false);

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
		Assert.notNull(owner, "not.allowed");
		
		Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "OWNER"), "not.allowed");
		Assert.isTrue(owner.equals((Owner) principal), "not.allowed");

		this.roomService.deleteRooms(owner.getId());
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

		/* Email */
		if (!form.getEmail().isEmpty()) {
			try {
				Assert.isTrue(this.utilityService.checkEmail(form.getEmail(), "OWNER"));

			} catch (Throwable oops) {
				binding.rejectValue("email", "email.error");
			}
		}
		
		/* Password confirmation */
		if (!form.getPassword().isEmpty() && !form.getPasswordConfirmation().isEmpty()) {
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
		return res;
	}

	public Owner reconstruct(ActorForm actorEditionForm, BindingResult binding) {

		/* Creating owner */
		Owner res = this.create();
		Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "OWNER"), "not.allowed");
		Assert.isTrue(principal.getId() == actorEditionForm.getId(), "not.allowed");

		Owner aux = this.findOne(actorEditionForm.getId());
		
		res.setId(aux.getId());
		res.setVersion(aux.getVersion());
		res.setIsSpammer(aux.getIsSpammer());
		
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
		
		/* Email */
		if (!actorEditionForm.getEmail().isEmpty()) {
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

		return res;
	}

	public Owner findByUsername(final String username) {
		return this.ownerRepository.findByUsername(username);
	}

	public void flush() {
		this.ownerRepository.flush();
	}
	
	public String exportData() {
		Actor owner = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(owner, "OWNER"), "not.allowed");
		
		String res;
		
		res = "Data of your user account:";
		res += "\r\n\r\n";
		res += "Name: " + owner.getName()
				+ " \r\n" + "Middle Name: " + owner.getMiddleName() + " \r\n"
				+ " \r\n" + "Surname: "	+ owner.getSurname() + " \r\n"
				+ " \r\n" + "Photo: " + owner.getPhoto() + " \r\n" + "Email: "
				+ owner.getEmail() + " \r\n" + "Phone Number: "
				+ owner.getPhoneNumber() + " \r\n" + "Address: "
				+ owner.getAddress() + " \r\n" + " \r\n" + "\r\n";
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		res += "\r\n\r\n";

		res += "Rooms::";
		res += "\r\n\r\n";
		Collection<Room> rooms = this.roomService.findRoomsMine(owner.getId());
		for (Room room : rooms) {
			res += "Room: " + "\r\n\r\n";
			res += "Title: " + room.getTitle()+ "\r\n\r\n";
			res += "Ticker: " + room.getTicker()+ "\r\n\r\n";
			res += "Status: " + room.getStatus()+ "\r\n\r\n";
			res += "Addres: " + room.getAddress()+ "\r\n\r\n";
			res += "Schedule details: " + room.getScheduleDetails()+ "\r\n\r\n";
			res += "Opening hour: " + room.getOpeningHour()+ "\r\n\r\n";
			res += "Closing hour" + room.getClosingHour()+ "\r\n\r\n";
			res += "Prove of ownership: " + room.getProveOfOwnership()+ "\r\n\r\n";
			res += "Photos: " + room.getPhotos()+ "\r\n\r\n";
			res += "Capacity: " + room.getCapacity()+ "\r\n\r\n";
			res += "Administrator: " + room.getAdministrator()+ "\r\n\r\n";
			res += "Categories: ";
			
			Collection<Category> categoriesOfRoom = room.getCategories();
			res+= " -- ";
			for(Category category : categoriesOfRoom) {
				res+= category.getTitle().get("English");
				res+= " -- ";
			}
			res+= "\r\n\r\n\r\n\r\n";
			
			res += "Services: " + "\r\n\r\n";
			
			Collection<domain.Service> servicesOfRoom = this.serviceService.findServicesByRoomId(room.getId());
			
			for(domain.Service service : servicesOfRoom) {
				res += "Service: " + "\r\n\r\n";
				res += "Name: " + service.getName()+ "\r\n\r\n";
				res += "Description: " + service.getDescription()+ "\r\n\r\n";
				res += "Price: " + service.getPrice()+ "\r\n\r\n";
				
				res+= "\r\n\r\n";
				res += "------";
				res+= "\r\n\r\n";
			}
			res+= "\r\n\r\n";
			res += "-----------";
			res+= "\r\n\r\n";
			
		}
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		
		return res;
	}
}
