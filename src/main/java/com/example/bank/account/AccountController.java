package com.example.bank.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount (Account account){
        return accountService.createAccount(account);
    }

    @GetMapping
    public Iterable<Account> getAccounts(){
        return accountService.getAccounts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById (@PathVariable("id") Long id){
        return accountService.getAccountById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount (@PathVariable("id") Long id, @RequestBody Account account){
        return accountService.updateAccount(id, account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") Long id){
        return accountService.deleteAccount(id) ;
    }


}
