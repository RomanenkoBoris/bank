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
    public ResponseEntity<Account> createAccount (AccountDTO accountDTO){
        return accountService.createAccount(accountDTO);
    }

    @GetMapping
    public Iterable<Account> getAccounts(){
        return accountService.getAccounts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAccountById (@PathVariable("id") Long id){
        return accountService.getAccountById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount (@PathVariable("id") Long id, @RequestBody AccountDTO accountDTO){
        return accountService.updateAccount(id, accountDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") Long id){
        return accountService.deleteAccount(id) ;
    }


}
