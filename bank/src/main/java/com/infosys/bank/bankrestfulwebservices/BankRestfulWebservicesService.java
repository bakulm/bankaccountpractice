package com.infosys.bank.bankrestfulwebservices;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.infosys.bank.jpa.BankAccountRepository;

@Service
public class BankRestfulWebservicesService {

	private BankAccountRepository bankAccountRepository;

	public BankRestfulWebservicesService(BankAccountRepository bankAccountRepository) {
		this.bankAccountRepository = bankAccountRepository;
	}

	public ResponseEntity<BankAccountEntity> createBankAccountUser(BankAccountEntity bankAccountEntity) {

		BankAccountEntity savedbankAccountEntity = bankAccountRepository.save(bankAccountEntity);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedbankAccountEntity.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

	public EntityModel<BankAccountEntity> retrieveUser(int id) {
		Optional<BankAccountEntity> bankaccountuser = bankAccountRepository.findById(id);

		if (bankaccountuser.isEmpty())
			throw new BankAccountNotFoundException("id:" + id);

		EntityModel<BankAccountEntity> entityModel = EntityModel.of(bankaccountuser.get());

		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveBankAccountAllUsers());
		entityModel.add(link.withRel("all-bak-account-users"));

		return entityModel;
	}

	public void deleteBankAccountUser(int id) {
		bankAccountRepository.deleteById(id);
	}

	public ResponseEntity<BankAccountEntity> updateBankAccountUser(int id, Map<String, ?> bankAccountEntity) {
		Optional<BankAccountEntity> bankAccountUserlData = bankAccountRepository.findById(id);

		if (bankAccountUserlData.isPresent()) {
			BankAccountEntity _bankAccountEntity = bankAccountUserlData.get();
			if (bankAccountEntity.containsKey("firstName")) {
				_bankAccountEntity.setFirstName(bankAccountEntity.get("firstName").toString());
			}
			if (bankAccountEntity.containsKey("lastName")) {
				_bankAccountEntity.setLastName(bankAccountEntity.get("lastName").toString());
			}
			if (bankAccountEntity.containsKey("birthDate")) {
				_bankAccountEntity.setBirthDate(LocalDate.parse((CharSequence) bankAccountEntity.get("birthDate")));
			}
			if (bankAccountEntity.containsKey("accountBalance")) {
				_bankAccountEntity.setAccountBalance((int) bankAccountEntity.get("accountBalance"));
			}
			return new ResponseEntity<>(bankAccountRepository.save(_bankAccountEntity), HttpStatus.OK);
		} else {
			throw new BankAccountNotFoundException("id:" + id);
		}
	}

	public List<BankAccountEntity> retrieveBankAccountAllUsers() {
		return bankAccountRepository.findAll();
	}

}
