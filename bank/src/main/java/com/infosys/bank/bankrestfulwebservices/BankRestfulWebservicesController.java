package com.infosys.bank.bankrestfulwebservices;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

		return bankRestfulWebservicesService.retrieveUser(id);
	}

	@DeleteMapping("/bankaccountusers/{id}")
	public void deleteUser(@PathVariable int id) {
		
		bankRestfulWebservicesService.deleteBankAccountUser(id);
	}

	@PutMapping("/bankaccountusers/{id}")
	  public ResponseEntity<BankAccountEntity> updateBankAccountUser(@PathVariable int id, @RequestBody Map<String, ?> bankAccountEntity) {
	    
		 return bankRestfulWebservicesService.updateBankAccountUser(id, bankAccountEntity);
	}
}
