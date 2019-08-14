package forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import domain.Customer;

public class CustomerRegistrationForm {

	/* User Attributes */

	private int id, version;
	private String name, middleName, surname, photo, email, phoneNumber,
			address, username, password, passwordConfirmation;
	
	/* Credit Card attributes */
	private String holder;
	private String make;
	private String number;
	private Integer expirationMonth;
	private Integer expirationYear;
	private Integer CVV;
	
	/* Form attributes */
	private Boolean	termsAndConditions;

	/* Getters and setters */
	
	public CustomerRegistrationForm(Customer customer) {
		this.id = customer.getId();
		this.version = customer.getVersion();
		this.name = customer.getName();
		this.middleName = customer.getMiddleName();
		this.surname = customer.getSurname();
		this.photo = customer.getPhoto();
		this.email = customer.getEmail();
		this.phoneNumber = customer.getPhoneNumber();
		this.address = customer.getAddress();
		
		if(customer.getCreditCard() != null) {
			this.holder = customer.getCreditCard().getHolder();
			this.make = customer.getCreditCard().getMake();
			this.number = customer.getCreditCard().getNumber();
			this.expirationMonth = customer.getCreditCard().getExpirationMonth();
			this.expirationYear = customer.getCreditCard().getExpirationYear();
			this.CVV = customer.getCreditCard().getCVV();
		}
	}

	public CustomerRegistrationForm() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotBlank
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@URL
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@NotBlank
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@NotBlank
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@NotBlank
	@Size(min = 5, max = 32)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@NotBlank
	@Size(min = 5, max = 32)
	public String getPasswordConfirmation() {
		return this.passwordConfirmation;
	}

	public void setPasswordConfirmation(final String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
	
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	@CreditCardNumber
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Range(min = 1, max = 12)
	public Integer getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(Integer expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	@Range(min = 0, max = 99)
	public Integer getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(Integer expirationYear) {
		this.expirationYear = expirationYear;
	}

	@Range(min = 0, max = 999)
	public Integer getCVV() {
		return CVV;
	}

	public void setCVV(Integer CVV) {
		this.CVV = CVV;
	}
	
	@NotNull
	public Boolean getTermsAndConditions() {
		return this.termsAndConditions;
	}

	public void setTermsAndConditions(final Boolean termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}

}
