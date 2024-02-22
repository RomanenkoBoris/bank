package com.example.bank.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount (Account account){
        return accountService.createAccount(account);
    }

    @GetMapping("/accounts")
    public Iterable<Account> getAccounts(){
        return accountService.getAccounts();
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccountById (@PathVariable("id") Long id){
        return accountService.getAccountById(id);
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<Account> updateAccount (@PathVariable("id") Long id, @RequestBody Account account){
        return accountService.updateAccount(id, account);
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") Long id){
        return accountService.deleteAccount(id) ;
    }


}
