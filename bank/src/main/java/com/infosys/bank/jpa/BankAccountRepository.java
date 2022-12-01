package com.infosys.bank.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infosys.bank.bankrestfulwebservices.BankAccountEntity;


public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Integer> {

}
