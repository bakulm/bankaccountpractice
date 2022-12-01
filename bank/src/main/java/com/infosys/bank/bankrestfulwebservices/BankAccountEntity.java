package com.infosys.bank.bankrestfulwebservices;

import java.time.LocalDate;
import java.util.List;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

@Entity(name = "bank_account")
public class BankAccountEntity {

	protected BankAccountEntity() {

	}

	@Id
	@GeneratedValue
	private Integer id;

	@Size(min = 2, message = "First Name should have atleast 2 characters")
	// @JsonProperty("user_name")
	private String firstName;

	@Size(min = 2, message = "Last Name should have atleast 2 characters")
	// @JsonProperty("user_name")
	private String lastName;

	@Past(message = "Birth Date should be in the past")
	// @JsonProperty("birth_date")
	private LocalDate birthDate;

	@Min(0)
	@NonNull()
	// @JsonProperty("user_name")
	private int accountBalance;

	public BankAccountEntity(Integer id,
			String firstName,
			String lastName, 
			LocalDate birthDate,
			int accountBalance) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.accountBalance = accountBalance;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public int getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(int accountBalance) {
		this.accountBalance = accountBalance;
	}
	
	
	@Override
	public String toString() {
		return "BankAccountEntity [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", birthDate="
				+ birthDate + ", accountBalance=" + accountBalance + "]";
	}

	

}
