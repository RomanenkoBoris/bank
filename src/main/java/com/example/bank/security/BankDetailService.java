package com.example.bank.security;

import com.example.bank.client.Client;
import com.example.bank.client.ClientRepository;
import com.example.bank.manager.Manager;
import com.example.bank.manager.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BankDetailService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ManagerRepository managerRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findClientByLogin(username);
        Manager manager = managerRepository.findManagerByLogin(username);
        if(client == null && manager == null){
            throw new UsernameNotFoundException("User with username " + username + " not found");
        } else if(client != null){
            return new ClientDetails(client);
        }
        return new ManagerDetails(manager);
    }

}
