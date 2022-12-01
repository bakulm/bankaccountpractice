package com.infosys.bank.bankrestfulwebservices;

import java.io.Serializable;
import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.infosys.bank.jpa.BankAccountRepository;
import com.infosys.bank.jpa.TransactionRepository;

@Service
public class BankRestfulWebservicesService {

	private BankAccountRepository bankAccountRepository;
	private TransactionRepository transactionRepository;

	public BankRestfulWebservicesService(BankAccountRepository bankAccountRepository,
			TransactionRepository transactionRepository) {
		this.bankAccountRepository = bankAccountRepository;
		this.transactionRepository = transactionRepository;
	}

	public ResponseEntity<BankAccountEntity> createBankAccountUser(BankAccountEntity bankAccountEntity) {

		BankAccountEntity savedbankAccountEntity = bankAccountRepository.save(bankAccountEntity);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedbankAccountEntity.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

	public Optional<BankAccountEntity> retrieveUser(int id) {
		Optional<BankAccountEntity> bankaccountuser = bankAccountRepository.findById(id);

		if (bankaccountuser.isEmpty())
			throw new BankAccountNotFoundException("id:" + id);

		return bankaccountuser;
	}

	public void deleteBankAccountUser(int id) {
		bankAccountRepository.deleteById(id);
	}

	public ResponseEntity<BankAccountEntity> updateBankAccountUser(int id, Map<String, ?> bankAccountEntity) {
		Optional<BankAccountEntity> bankAccountUserlData = bankAccountRepository.findById(id);
		System.out.println("      " + id + "finalBalance" + bankAccountEntity.get("accountBalance") + "%%%"
				+ bankAccountUserlData);

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

	public TransactionEntity withdrawForUser(int id, TransactionEntity transactionEntity) {

		Optional<BankAccountEntity> bankAccountUserlData = bankAccountRepository.findById(id);

		if (bankAccountUserlData.isEmpty())
			throw new BankAccountNotFoundException("id:" + id);
		EntityModel<BankAccountEntity> entityModel = EntityModel.of(bankAccountUserlData.get());

		int finalBalance = entityModel.getContent().getAccountBalance() - transactionEntity.getAmount();

		Map<String, Serializable> balance = new HashMap<>();

		balance.put("accountBalance", finalBalance);

		updateBankAccountUser(id, balance);

		transactionEntity.setBankAccountEntity(bankAccountUserlData.get());

		return transactionRepository.save(transactionEntity);

	}

	public TransactionEntity creditForUser(int id, TransactionEntity transactionEntity) {

		Optional<BankAccountEntity> bankAccountUserlData = bankAccountRepository.findById(id);

		if (bankAccountUserlData.isEmpty())
			throw new BankAccountNotFoundException("id:" + id);
		EntityModel<BankAccountEntity> entityModel = EntityModel.of(bankAccountUserlData.get());

		int finalBalance = entityModel.getContent().getAccountBalance() + transactionEntity.getAmount();

		Map<String, Serializable> balance = new HashMap<>();

		balance.put("accountBalance", finalBalance);

		updateBankAccountUser(id, balance);

		transactionEntity.setBankAccountEntity(bankAccountUserlData.get());

		return transactionRepository.save(transactionEntity);

	}
}
