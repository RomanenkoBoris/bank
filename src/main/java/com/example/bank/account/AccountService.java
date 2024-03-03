package com.example.bank.account;

import com.example.bank.client.ClientRepository;
import com.example.bank.manager.Manager;
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
import java.util.function.Predicate;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    ClientRepository clientRepository;

    protected ResponseEntity<Account> createAccount(AccountDTO accountDTO){
        ManagerDetails managerDetails = (ManagerDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasManagerPermission =
                managerDetails.getAuthorities().stream().anyMatch(auth -> auth.toString().equals("ROLE_REGIONAL_MANAGER")) ||
                managerDetails.getAuthorities().stream().anyMatch(auth -> auth.toString().equals("ROLE_BRANCH_MANAGER"))||
                clientRepository.findById(accountDTO.clientId()).get().getManager().getLogin().equals(managerDetails.getUsername());
        if (hasManagerPermission){
            return new ResponseEntity<>(accountRepository.save(accountMapper.toEntity(accountDTO)), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    protected List<Account> getAccounts (){
        ManagerDetails managerDetails = (ManagerDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isSupervisor =
                managerDetails.getAuthorities().stream().anyMatch(auth -> auth.toString().equals("ROLE_REGIONAL_MANAGER")) ||
                        managerDetails.getAuthorities().stream().anyMatch(auth -> auth.toString().equals("ROLE_BRANCH_MANAGER"));
        if(isSupervisor){
            return accountRepository.findAll();
        }
        Manager manager = managerRepository.findManagerByLogin(managerDetails.getUsername());
        return accountRepository.findAll().stream()
                .filter(account -> account.getClient().getManager().equals(manager))
                .toList();
    }

    protected ResponseEntity<Object> getAccountById (Long id){
        if(!accountRepository.existsById(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Account account = accountRepository.findById(id).get();
        AccountDTO accountDTO = accountMapper.toDTO(account);
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClientDetails clientDetails;
        ManagerDetails managerDetails;
        boolean hasClientPermission = false;
        boolean hasManagerPermission = false;
        if(user instanceof ClientDetails){
            clientDetails = (ClientDetails) user;
            hasClientPermission = clientDetails.getUsername().equals(account.getClient().getLogin());
        } else {
            managerDetails = (ManagerDetails) user;
            hasManagerPermission =
                    managerDetails.getAuthorities().stream().anyMatch(auth -> auth.toString().equals("ROLE_REGIONAL_MANAGER")) ||
                    managerDetails.getAuthorities().stream().anyMatch(auth -> auth.toString().equals("ROLE_BRANCH_MANAGER")) ||
                    account.getClient().getManager().getLogin().equals(managerDetails.getUsername());
        }
        if (hasClientPermission) {
            return new ResponseEntity<>(accountDTO, HttpStatus.OK);
        } else if (hasManagerPermission){
            return new ResponseEntity<>(account, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    protected ResponseEntity<Account> updateAccount (Long id, AccountDTO accountDTO){
        if (accountRepository.existsById(id)){
            Account tmp = accountRepository.findById(id).orElse(null);
            tmp.setName(accountDTO.name());
            tmp.setAccountType(accountDTO.accountType());
            tmp.setAccountStatus(accountDTO.accountStatus());
            tmp.setBalance(accountDTO.balance());
            tmp.setCurrencyCode(accountDTO.currencyCode());
            tmp.setLastModifiedTime(LocalDateTime.now());
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
                managerDetails.getAuthorities().stream().anyMatch(auth -> auth.toString().equals("ROLE_REGIONAL_MANAGER")) ||
                        managerDetails.getAuthorities().stream().anyMatch(auth -> auth.toString().equals("ROLE_BRANCH_MANAGER")) ||
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
