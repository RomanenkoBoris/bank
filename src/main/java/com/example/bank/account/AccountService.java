package com.example.bank.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;


    protected ResponseEntity<Account> createAccount(Account account){
        return new ResponseEntity<>(accountRepository.save(account), HttpStatus.OK);
    }
    protected List<Account> getAccounts (){
        return accountRepository.findAll();
    }

    protected ResponseEntity<Account> getAccountById (Long id){
        if (accountRepository.existsById(id)){
            return new ResponseEntity<>(accountRepository.findById(id).get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    protected ResponseEntity<Account> updateAccount (Long id, Account account){
        if (accountRepository.existsById(id) && account.getId().equals(id)){
            Account tmp = accountRepository.findById(id).orElse(null);
            tmp.setName(account.getName());
            tmp.setAccountType(account.getAccountType());
            tmp.setAccountStatus(account.getAccountStatus());
            tmp.setBalance(account.getBalance());
            tmp.setCurrencyCode(account.getCurrencyCode());
            tmp.setCreationTime(account.getCreationTime());
            tmp.setLastModifiedTime(account.getLastModifiedTime());
            // tmp.setClient(account.getClient()); чтобы обновлять клиента отсюда, нужно добавить класс аккаунтДТО, который в сервисе мапить на конкретный аккаунт и так уже и менять клиента
            return new ResponseEntity<>(accountRepository.save(tmp), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    protected ResponseEntity<Void> deleteAccount(Long id){
        if(accountRepository.existsById(id)){
            accountRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
