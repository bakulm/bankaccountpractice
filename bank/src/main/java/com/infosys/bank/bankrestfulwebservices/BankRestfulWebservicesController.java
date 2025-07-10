package com.infosys.bank.bankrestfulwebservices;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * REST Controller for Bank Account operations
 * Provides endpoints for CRUD operations and financial transactions
 */
@RestController
@RequestMapping("/api/v1/bank-accounts")
public class BankRestfulWebservicesController {

	@Autowired
	private BankRestfulWebservicesService bankRestfulWebservicesService;

	/**
	 * Creates a new bank account
	 * @param bankAccountEntity the bank account details
	 * @return ResponseEntity with created bank account and location header
	 */
	@PostMapping
	public ResponseEntity<BankAccountEntity> createBankAccount(@Valid @RequestBody BankAccountEntity bankAccountEntity) {
		return bankRestfulWebservicesService.createBankAccountUser(bankAccountEntity);
	}

	/**
	 * Retrieves all bank accounts with HATEOAS links
	 * @return Collection of bank accounts with navigation links
	 */
	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<BankAccountEntity>>> retrieveAllBankAccounts() {
		List<BankAccountEntity> bankAccounts = bankRestfulWebservicesService.retrieveBankAccountAllUsers();
		
		List<EntityModel<BankAccountEntity>> bankAccountModels = bankAccounts.stream()
			.map(account -> EntityModel.of(account)
				.add(linkTo(methodOn(this.getClass()).retrieveBankAccount(account.getId())).withSelfRel())
				.add(linkTo(methodOn(this.getClass()).deleteBankAccount(account.getId())).withRel("delete"))
				.add(linkTo(methodOn(this.getClass()).updateBankAccount(account.getId(), null)).withRel("update")))
			.collect(Collectors.toList());
		
		CollectionModel<EntityModel<BankAccountEntity>> collectionModel = CollectionModel.of(bankAccountModels);
		collectionModel.add(linkTo(methodOn(this.getClass()).retrieveAllBankAccounts()).withSelfRel());
		collectionModel.add(linkTo(methodOn(this.getClass()).createBankAccount(null)).withRel("create"));
		
		return ResponseEntity.ok(collectionModel);
	}

	/**
	 * Retrieves a specific bank account by ID with HATEOAS links
	 * @param id the bank account ID
	 * @return EntityModel with bank account details and navigation links
	 */
	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<BankAccountEntity>> retrieveBankAccount(@PathVariable int id) {
		BankAccountEntity bankAccount = bankRestfulWebservicesService.retrieveUser(id).get();
		
		EntityModel<BankAccountEntity> entityModel = EntityModel.of(bankAccount);
		
		// Add HATEOAS links
		entityModel.add(linkTo(methodOn(this.getClass()).retrieveBankAccount(id)).withSelfRel());
		entityModel.add(linkTo(methodOn(this.getClass()).retrieveAllBankAccounts()).withRel("all-bank-accounts"));
		entityModel.add(linkTo(methodOn(this.getClass()).updateBankAccount(id, null)).withRel("update"));
		entityModel.add(linkTo(methodOn(this.getClass()).deleteBankAccount(id)).withRel("delete"));
		entityModel.add(linkTo(methodOn(this.getClass()).withdrawFromAccount(id, null)).withRel("withdraw"));
		entityModel.add(linkTo(methodOn(this.getClass()).creditToAccount(id, null)).withRel("credit"));
		
		return ResponseEntity.ok(entityModel);
	}

	/**
	 * Deletes a bank account by ID
	 * @param id the bank account ID to delete
	 * @return ResponseEntity with no content
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBankAccount(@PathVariable int id) {
		bankRestfulWebservicesService.deleteBankAccountUser(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Updates a bank account with partial data
	 * @param id the bank account ID
	 * @param bankAccountEntity partial bank account data to update
	 * @return ResponseEntity with updated bank account
	 */
	@PutMapping("/{id}")
	public ResponseEntity<BankAccountEntity> updateBankAccount(@PathVariable int id,
			@Valid @RequestBody Map<String, ?> bankAccountEntity) {
		return bankRestfulWebservicesService.updateBankAccountUser(id, bankAccountEntity);
	}

	/**
	 * Withdraws money from a bank account
	 * @param id the bank account ID
	 * @param transactionEntity the withdrawal transaction details
	 * @return ResponseEntity with transaction details
	 */
	@PostMapping("/{id}/transactions/withdraw")
	public ResponseEntity<EntityModel<TransactionEntity>> withdrawFromAccount(@PathVariable int id,
			@Valid @RequestBody TransactionEntity transactionEntity) {
		
		TransactionEntity savedTransaction = bankRestfulWebservicesService.withdrawForUser(id, transactionEntity);
		
		EntityModel<TransactionEntity> entityModel = EntityModel.of(savedTransaction);
		entityModel.add(linkTo(methodOn(this.getClass()).retrieveBankAccount(id)).withRel("account"));
		entityModel.add(linkTo(methodOn(this.getClass()).creditToAccount(id, null)).withRel("credit"));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
	}

	/**
	 * Credits money to a bank account
	 * @param id the bank account ID
	 * @param transactionEntity the credit transaction details
	 * @return ResponseEntity with transaction details
	 */
	@PostMapping("/{id}/transactions/credit")
	public ResponseEntity<EntityModel<TransactionEntity>> creditToAccount(@PathVariable int id,
			@Valid @RequestBody TransactionEntity transactionEntity) {
		
		TransactionEntity savedTransaction = bankRestfulWebservicesService.creditForUser(id, transactionEntity);
		
		EntityModel<TransactionEntity> entityModel = EntityModel.of(savedTransaction);
		entityModel.add(linkTo(methodOn(this.getClass()).retrieveBankAccount(id)).withRel("account"));
		entityModel.add(linkTo(methodOn(this.getClass()).withdrawFromAccount(id, null)).withRel("withdraw"));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
	}
}
