
package services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
	private ActorRepository				actorRepository;

	@Autowired
	private RoomService					roomService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	// Other business methods -------------------------------

	public Boolean checkEmail(final String email, final String authority) {
		Boolean result;
		final String emailLowerCase = email.toLowerCase();

		final Pattern pattern = Pattern.compile("(^(([a-z]|[0-9]){1,}[@]{1}([a-z]|[0-9]){1,}([.]{1}([a-z]|[0-9]){1,}){1,})$)|(^((([a-z]|[0-9]){1,}[ ]{1}){1,}<(([a-z]|[0-9]){1,}[@]{1}([a-z]|[0-9]){1,}([.]{1}([a-z]|[0-9]){1,}){1,})>)$)");
		final Matcher matcher = pattern.matcher(emailLowerCase);
		if (authority.equals("ADMIN")) {
			final Pattern patternAdmin = Pattern.compile("(^((([a-z]|[0-9]){1,}[@])$)|(^(([a-z]|[0-9]){1,}[ ]{1}){1,}<(([a-z]|[0-9]){1,}[@]>))$)");
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

	public Collection<Actor> findAllExceptPrincipal() {
		Actor actor = null;
		Collection<Actor> result = new ArrayList<>();
		final UserAccount userAccount = LoginService.getPrincipal();
		actor = this.actorRepository.findByUserAccountId(userAccount.getId());
		result = this.actorRepository.findAll();
		result.remove(actor);
		return result;
	}

	public boolean checkAuthority(final Actor actor, final String authority) {
		Assert.notNull(actor, "not.allowed");
		boolean result = false;
		if (actor.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(authority))
			result = true;
		return result;
	}

	public Boolean existsUsername(final String username) {
		return !(this.actorRepository.existsUsername(username) != null);
	}

	public boolean isAdmin() {
		boolean isAdmin = false;

		try {
			isAdmin = this.checkAuthority(this.findByPrincipal(), "ADMIN");
		} catch (final Throwable oops) {

		}
		return isAdmin;
	}
	public boolean isSpam(final List<String> atributosAComprobar) {
		boolean containsSpam = false;
		final String[] spamWords = this.systemConfigurationService.findMySystemConfiguration().getSpamWords().split(",");
		for (int i = 0; i < atributosAComprobar.size(); i++)
			if (containsSpam == false) {
				for (final String spamWord : spamWords)
					if (atributosAComprobar.get(i).toLowerCase().contains(spamWord.toLowerCase())) {
						containsSpam = true;
						break;
					}
			} else
				break;
		return containsSpam;
	}

	public String generateTicker(final Owner owner) {
		String uniqueTicker = null;
		String mName, add, alphaNum, address;
		boolean unique = false;

		if (owner.getMiddleName() != null) {
			String middleName = owner.getMiddleName().replaceAll("[0-9]|,|/| ", "");
			middleName = middleName.toUpperCase();
			if (middleName.length() >= 4)
				mName = owner.getMiddleName().substring(0, 4);
			else
				mName = owner.getMiddleName().substring(0, owner.getMiddleName().length());
			if (mName.length() < 4)
				for (int length = 4 - middleName.length(); length != 0; length--)
					mName = mName.concat("M");
		} else
			mName = "MMM";

		address = owner.getAddress().replaceAll("[0-9]|,|/| ", "");
		address = address.toUpperCase();

		if (address.length() >= 4)
			add = address.substring(0, 4);
		else
			add = address.substring(0, owner.getAddress().length());
		if (add.length() < 4)
			for (int length = 4 - add.length(); length != 0; length--)
				add = address.concat("A");

		while (unique == false) {
			alphaNum = this.randomStringTicker();
			uniqueTicker = mName + add + "-" + alphaNum;
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
			stringBuilder.append(possibleChars.charAt(rnd.nextInt(possibleChars.length())));
		return stringBuilder.toString();

	}

	private boolean checkForUniqueTicket(final String ticker) {
		return this.roomService.uniqueTicket(ticker);
	}
	
	public boolean isValidCCMake (String toValidate) {
		boolean isValid = false;
		String[] aux = this.systemConfigurationService.findMySystemConfiguration().getMakers().split(",");
		for(String validMake : aux) {
			if(toValidate.contentEquals(validMake)) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}
}
