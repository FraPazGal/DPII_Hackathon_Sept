
package domain;

import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class SystemConfiguration extends DomainEntity {

	/* Attributes */

	private String				systemName, banner, countryCode, makers, spamWords;
	private Map<String, String>	welcomeMessage;
	private Integer				timeResultsCached, maxResults;
	private Double				VATTax;


	/* Getters and setters */

	@NotBlank
	public String getSystemName() {
		return this.systemName;
	}

	public void setSystemName(final String systemName) {
		this.systemName = systemName;
	}

	@NotBlank
	@URL
	public String getBanner() {
		return this.banner;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	@NotBlank
	@Pattern(regexp = "[+]\\d{3}")
	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(final String countryCode) {
		this.countryCode = countryCode;
	}

	@NotBlank
	public String getMakers() {
		return this.makers;
	}

	public void setMakers(final String makers) {
		this.makers = makers;
	}

	@NotEmpty
	@ElementCollection
	public Map<String, String> getWelcomeMessage() {
		return this.welcomeMessage;
	}

	public void setWelcomeMessage(final Map<String, String> welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	public String getSpamWords() {
		return this.spamWords;
	}

	public void setSpamWords(final String spamWords) {
		this.spamWords = spamWords;
	}

	@NotNull
	@Range(min = 1, max = 24)
	public Integer getTimeResultsCached() {
		return this.timeResultsCached;
	}

	public void setTimeResultsCached(final Integer timeResultsCached) {
		this.timeResultsCached = timeResultsCached;
	}

	@NotNull
	@Range(min = 0, max = 100)
	public Integer getMaxResults() {
		return this.maxResults;
	}

	public void setMaxResults(final Integer maxResults) {
		this.maxResults = maxResults;
	}

	@NotNull
	@Range(min = 0, max = 1)
	public Double getVATTax() {
		return this.VATTax;
	}

	public void setVATTax(final Double vATTax) {
		this.VATTax = vATTax;
	}

}
