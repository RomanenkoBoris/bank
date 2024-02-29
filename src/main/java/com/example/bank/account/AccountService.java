package com.example.bank.account;

import com.example.bank.client.ClientRepository;
import com.example.bank.manager.ManagerRepository;
import com.example.bank.security.ClientDetails;
import com.example.bank.security.ManagerDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    AccountMapper accountMapper;

    protected ResponseEntity<Account> createAccount(AccountDTO accountDTO){
        return new ResponseEntity<>(accountRepository.save(accountMapper.toEntity(accountDTO)), HttpStatus.OK);
    }
    protected List<Account> getAccounts (){
        return accountRepository.findAll();
    }

    protected ResponseEntity<Account> getAccountById (Long id){
        if(!accountRepository.existsById(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Account account = accountRepository.findById(id).get();
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClientDetails clientDetails;
        ManagerDetails managerDetails;
        boolean isClientHasPermission = false;
        boolean isManagerHasPermission = false;
        if(user instanceof ClientDetails){
            clientDetails = (ClientDetails) user;
            isClientHasPermission = clientDetails.getUsername().equals(account.getClient().getLogin());
        } else {
            managerDetails = (ManagerDetails) user;
            isManagerHasPermission = (
                    managerDetails.getAuthorities().stream().anyMatch(auth -> auth.toString().equals("REGIONAL_MANAGER")) ||
                    managerDetails.getAuthorities().stream().anyMatch(auth -> auth.toString().equals("BRANCH_MANAGER")) ||
                    account.getClient().getManager().getLogin().equals(managerDetails.getUsername()));
        }
        if (isClientHasPermission || isManagerHasPermission) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
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
        if(!accountRepository.existsById(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Account account = accountRepository.findById(id).get();
        ManagerDetails managerDetails =(ManagerDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isManagerHasPermission = (
                managerDetails.getAuthorities().stream().anyMatch(auth -> auth.toString().equals("REGIONAL_MANAGER")) ||
                        managerDetails.getAuthorities().stream().anyMatch(auth -> auth.toString().equals("BRANCH_MANAGER")) ||
                        account.getClient().getManager().getLogin().equals(managerDetails.getUsername()));
        if (isManagerHasPermission){
            account.setAccountStatus(AccountStatus.CLOSED);
            account.setLastModifiedTime(LocalDateTime.now());
            accountRepository.save(account);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}
