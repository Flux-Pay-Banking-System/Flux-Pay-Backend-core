package com.bank.account.service.impl;

import com.bank.account.constants.AccountConstants;
import com.bank.account.dto.AccountsDto;
import com.bank.account.dto.CustomerDto;
import com.bank.account.entity.Accounts;
import com.bank.account.entity.Customer;
import com.bank.account.exception.CustomerAlreadyExistException;
import com.bank.account.exception.ResourceNotFoundException;
import com.bank.account.mapper.AccountMapper;
import com.bank.account.mapper.CustomerMapper;
import com.bank.account.repository.AccountRepository;
import com.bank.account.repository.CustomerRepository;
import com.bank.account.service.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> customerOpt = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (customerOpt.isPresent()) {
            throw new CustomerAlreadyExistException("Customer already registered with given mobileNumber "
            + customerDto.getMobileNumber());

        }
        Customer saveCustomer = customerRepository.save(customer);
        accountRepository.save(createNewAccount(saveCustomer));
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer",  "mobileNumber", mobileNumber)
        );

        Accounts accounts = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account",  "customerId", mobileNumber)
        );

        CustomerDto customerDto= CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountMapper.mapToAccountsDto(accounts,new AccountsDto()));

        return customerDto;


    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated=false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto !=null){
            Accounts accounts = accountRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountMapper.mapToAccounts(accountsDto, accounts);
            accounts =accountRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer",  "customerId", customerId.toString())
            );

            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated=true;

        }
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer",  "mobileNumber", mobileNumber)
        );

        accountRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountConstants.SAVINGS);
        newAccount.setBranchAddress(AccountConstants.ADDRESS);
        return newAccount;
    }
}
