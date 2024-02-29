package com.example.bank.account;

import com.example.bank.client.Client;
import com.example.bank.client.ClientRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

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
    @Mapping(target = "creationTime", expression = "java(LocalDate.now())")
    @Mapping(target = "lastModifiedTime", expression = "java(LocalDate.now())")
    @Mapping(source = "clientId", qualifiedByName = "findClient", target = "client")
    protected abstract Account toEntity(AccountDTO accountDTO);

    @Named("findClient")
    private Client findClient (Long clientId){
        if(clientRepository.existsById(clientId)){
            return clientRepository.findById(clientId).get();
        } else {
            return null;
        }
    }

}
