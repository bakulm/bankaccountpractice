package com.infosys.bank.bankrestfulwebservices;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
public class BankRestfulWebservicesController {

	@Autowired
	private BankRestfulWebservicesService bankRestfulWebservicesService;

	@PostMapping("/createbankaccount")
	public ResponseEntity<BankAccountEntity> createUser(@Valid @RequestBody BankAccountEntity bankAccountEntity) {

		return bankRestfulWebservicesService.createBankAccountUser(bankAccountEntity);
	}

	@GetMapping("/bankaccountusers")
	public List<BankAccountEntity> retrieveAllUsers() {

		return bankRestfulWebservicesService.retrieveBankAccountAllUsers();
	}

	@GetMapping("/bankaccountusers/{id}")
	public EntityModel<BankAccountEntity> retrieveUser(@PathVariable int id) {
		EntityModel<BankAccountEntity> entityModel = EntityModel
				.of(bankRestfulWebservicesService.retrieveUser(id).get());

		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-bak-account-users"));
		return entityModel;
	}

	@DeleteMapping("/bankaccountusers/{id}")
	public void deleteUser(@PathVariable int id) {

		bankRestfulWebservicesService.deleteBankAccountUser(id);
	}

	@PutMapping("/bankaccountusers/{id}")
	public ResponseEntity<BankAccountEntity> updateBankAccountUser(@PathVariable int id,
			@RequestBody Map<String, ?> bankAccountEntity) {

		return bankRestfulWebservicesService.updateBankAccountUser(id, bankAccountEntity);
	}

	@PutMapping("/bankaccountusers/{id}/withdraw")
	public TransactionEntity withdrawForUser(@PathVariable int id,
			@Valid @RequestBody TransactionEntity transactionEntity) {
		System.out.println("id" + id + "******" + transactionEntity);
		TransactionEntity savedTransaction = bankRestfulWebservicesService.withdrawForUser(id, transactionEntity);

		return savedTransaction;

	}

	@PutMapping("/bankaccountusers/{id}/credit")
	public TransactionEntity creditForUser(@PathVariable int id,
			@Valid @RequestBody TransactionEntity transactionEntity) {
		
		TransactionEntity savedTransaction = bankRestfulWebservicesService.creditForUser(id, transactionEntity);
		return savedTransaction;

	}
}
