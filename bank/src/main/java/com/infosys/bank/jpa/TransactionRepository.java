package com.infosys.bank.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infosys.bank.bankrestfulwebservices.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {

}

