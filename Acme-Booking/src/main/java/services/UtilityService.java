package services;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Owner;

@Transactional
@Service
public class UtilityService {

	// Managed repository ------------------------------------
	
	@Autowired
	private ActorRepository actorRepository;
	
	@Autowired
	private RoomService roomService;	
	
	// Other business methods -------------------------------

	public Boolean checkEmail(final String email, final String authority) {
		Boolean result;
		String emailLowerCase = email.toLowerCase();

		final Pattern pattern = Pattern
				.compile("(^(([a-z]|[0-9]){1,}[@]{1}([a-z]|[0-9]){1,}([.]{1}([a-z]|[0-9]){1,}){1,})$)|(^((([a-z]|[0-9]){1,}[ ]{1}){1,}<(([a-z]|[0-9]){1,}[@]{1}([a-z]|[0-9]){1,}([.]{1}([a-z]|[0-9]){1,}){1,})>)$)");
		final Matcher matcher = pattern.matcher(emailLowerCase);
		if (authority.equals("ADMIN")) {
			final Pattern patternAdmin = Pattern
					.compile("(^((([a-z]|[0-9]){1,}[@])$)|(^(([a-z]|[0-9]){1,}[ ]{1}){1,}<(([a-z]|[0-9]){1,}[@]>))$)");
			final Matcher matcherAdmin = patternAdmin.matcher(emailLowerCase);
			result = matcherAdmin.matches() ? true : false;
		} else
			result = matcher.matches() ? true : false;
		return result;
	}

	public Actor findByPrincipal() {
		Actor result = null;
		final UserAccount userAccount = LoginService.getPrincipal();
		result = this.actorRepository.findByUserAccountId(userAccount.getId());
		return result;
	}
	
	public boolean checkAuthority(final Actor actor, final String authority) {
		Assert.notNull(actor, "not.allowed");
		boolean result = false;
		if (actor.getUserAccount().getAuthorities().iterator().next()
				.getAuthority().equals(authority))
			result = true;
		return result;
	}

	public Boolean existsUsername(String username) {
		return !(this.actorRepository.existsUsername(username) != null);
	}

	public boolean isAdmin() {
		boolean isAdmin = false;

		try {
			isAdmin = this.checkAuthority(this.findByPrincipal(), "ADMIN");
		} catch (Throwable oops) {

		}
		return isAdmin;
	}
	
	public String generateTicker(Owner owner) {
		String uniqueTicker = null;
		String middleName = null;
		String address = null, alphaNum;
		boolean unique = false;
		
		if(owner.getMiddleName() != null) {
			if(owner.getMiddleName().length() >= 4) {
				middleName = owner.getMiddleName().substring(0, 4);
			} else {
				middleName = owner.getMiddleName().substring(0, owner.getMiddleName().length());
			}
			if(middleName.length() < 4) {
				for(int length = 4 - middleName.length(); length != 0 ;length--) {
					middleName = middleName.concat("M");
				}
			}
		} else {
			middleName = "MMM";
		}
		
		if(owner.getAddress().length() >= 4) {
			address = owner.getAddress().substring(0, 4);
		} else {
			address = owner.getAddress().substring(0, owner.getAddress().length());
		}
		if(address.length() < 4) {
			for(int length = 4 - address.length(); length != 0 ;length--) {
				address = address.concat("A");
			}
		}
		
		
		while (unique == false) {
			alphaNum = this.randomStringTicker();
			uniqueTicker = middleName + address + "-" + alphaNum;
			unique = this.checkForUniqueTicket(uniqueTicker);
		}
		return uniqueTicker;
	}

	private String randomStringTicker() {

		final String possibleChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWYZ";
		final SecureRandom rnd = new SecureRandom();
		final int length = 6;

		final StringBuilder stringBuilder = new StringBuilder(length);

		for (int i = 0; i < length; i++)
			stringBuilder.append(possibleChars.charAt(rnd.nextInt(possibleChars
					.length())));
		return stringBuilder.toString();

	}
	
	private boolean checkForUniqueTicket(String ticker) {
		return this.roomService.uniqueTicket(ticker);
	}
}
