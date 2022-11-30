package com.infosys.bank.bankrestfulwebservices;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.infosys.bank.jpa.BankAccountRepository;

import jakarta.validation.Valid;

@RestController
public class BankRestfulWebservicesController {

	private BankAccountRepository bankAccountRepository;

	public BankRestfulWebservicesController(BankAccountRepository bankAccountRepository) {
		this.bankAccountRepository = bankAccountRepository;
	}

	
	@PostMapping("/createbankaccount")
	public ResponseEntity<BankAccountEntity> createUser(@Valid @RequestBody BankAccountEntity bankAccountEntity) {

		BankAccountEntity savedbankAccountEntity = bankAccountRepository.save(bankAccountEntity);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedbankAccountEntity.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping("/bankaccountusers")
	public List<BankAccountEntity> retrieveAllUsers() {
		return bankAccountRepository.findAll();
	}

}
