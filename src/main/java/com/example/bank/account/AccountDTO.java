package com.example.bank.account;

import java.math.BigDecimal;

public record AccountDTO
        (String name,
         AccountType accountType,
         AccountStatus accountStatus,
         BigDecimal balance,
         CurrencyCode currencyCode,
         Long clientId) {

}
