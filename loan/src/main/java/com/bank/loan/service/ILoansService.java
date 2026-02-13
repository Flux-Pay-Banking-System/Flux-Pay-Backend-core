package com.bank.loan.service;

import com.bank.loan.dto.LoansDto;

public interface ILoansService {
    void createLoans(String mobileNumber);
    LoansDto fetchLoansData(String mobileNumber);
    boolean updateLoans(LoansDto loansDto);
    boolean deleteLoans(String mobileNumber);
}
