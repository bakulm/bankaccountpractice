package com.infosys.bank.bankrestfulwebservices;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

	@GetMapping("/bankaccountusers/{id}")
	public EntityModel<BankAccountEntity> retrieveUser(@PathVariable int id) {
		Optional<BankAccountEntity> bankaccountuser = bankAccountRepository.findById(id);

		if (bankaccountuser.isEmpty())
			throw new BankAccountNotFoundException("id:" + id);

		EntityModel<BankAccountEntity> entityModel = EntityModel.of(bankaccountuser.get());

		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-bak-account-users"));

		return entityModel;
	}

	@DeleteMapping("/bankaccountusers/{id}")
	public void deleteUser(@PathVariable int id) {
		bankAccountRepository.deleteById(id);
	}
	
	 @PutMapping("/bankaccountusers/{id}")
	  public ResponseEntity<BankAccountEntity> updateBankAccountUser(@PathVariable int id, @RequestBody BankAccountEntity bankAccountEntity) {
	    Optional<BankAccountEntity> bankAccountUserlData = bankAccountRepository.findById(id);

	    if (bankAccountUserlData.isPresent()) {
	    	BankAccountEntity _bankAccountEntity = bankAccountUserlData.get();
	      _bankAccountEntity.setFirstName(bankAccountEntity.getFirstName());
	      _bankAccountEntity.setLastName(bankAccountEntity.getLastName());
	      _bankAccountEntity.setBirthDate(bankAccountEntity.getBirthDate());
	      _bankAccountEntity.setAccountBalance(bankAccountEntity.getAccountBalance());
	      return new ResponseEntity<>(bankAccountRepository.save(_bankAccountEntity), HttpStatus.OK);
	    } else {
	      throw new BankAccountNotFoundException("id:" + id);
	    }
	  }

}
