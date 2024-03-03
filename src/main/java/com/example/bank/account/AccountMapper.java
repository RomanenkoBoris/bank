package com.example.bank.account;

import com.example.bank.client.Client;
import com.example.bank.client.ClientRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.time.LocalDateTime;
import java.util.Optional;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class}, implementationName = "AccountMapperImpl")
public abstract class AccountMapper {

    @Autowired
    public ClientRepository clientRepository;

    @Mapping(source = "name", target = "name")
    @Mapping(source = "accountType", target = "accountType")
    @Mapping(source = "accountStatus", target = "accountStatus")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "currencyCode", target = "currencyCode")
    @Mapping(source = "account.client.id", target = "clientId") // account.???
    protected abstract AccountDTO toDTO(Account account);
    @Mapping(source = "name", target = "name")
    @Mapping(source = "accountType", target = "accountType")
    @Mapping(source = "accountStatus", target = "accountStatus")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "currencyCode", target = "currencyCode")
    @Mapping(target = "creationTime", expression = "java(LocalDateTime.now())")
    @Mapping(target = "lastModifiedTime", expression = "java(LocalDateTime.now())")
    @Mapping(source = "clientId", qualifiedByName = "findClient", target = "client")
    protected abstract Account toEntity(AccountDTO accountDTO);

    @Named(value = "findClient")
    public Client findClient (Long clientId) throws Exception {
        if (clientId != null) {
            Optional<Client> optionalClient = clientRepository.findById(clientId);

            if (optionalClient.isPresent()) {
                return optionalClient.get();
            } else {
                // Handle case when client with given id is not found
                throw new Exception("Client with ID " + clientId + " not found");
            }
        } else {
            // Handle case when clientId is null
            throw new IllegalArgumentException("Client ID must not be null");
        }
    }

}
