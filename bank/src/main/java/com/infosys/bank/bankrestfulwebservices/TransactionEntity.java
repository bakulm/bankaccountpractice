package com.infosys.bank.bankrestfulwebservices;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Entity
public class TransactionEntity {

	@Id
	@GeneratedValue
	private Integer id;

	@Size(min = 10)
	private String description;

	@Min(0)
	@NonNull()
	private int amount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private BankAccountEntity bankAccountEntity;

	public TransactionEntity(Integer id, String description, int amount, BankAccountEntity bankAccountEntity) {
		super();
		this.id = id;
		this.description = description;
		this.amount = amount;
		this.bankAccountEntity = bankAccountEntity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public BankAccountEntity getBankAccountEntity() {
		return bankAccountEntity;
	}

	public void setBankAccountEntity(BankAccountEntity bankAccountEntity) {
		this.bankAccountEntity = bankAccountEntity;
	}

	@Override
	public String toString() {
		return "TransactionEntity [id=" + id + ", description=" + description + ", amount=" + amount + "]";
	}

}
