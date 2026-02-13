package com.bank.loan.service.impl;

import com.bank.loan.LoanApplication;
import com.bank.loan.constants.LoansConstants;
import com.bank.loan.dto.LoansDto;
import com.bank.loan.entity.Loans;
import com.bank.loan.exception.LoanAlreadyExistsException;
import com.bank.loan.exception.ResourceNotFoundException;
import com.bank.loan.mapper.LoansMapper;
import com.bank.loan.repository.LoansRepository;
import com.bank.loan.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class LoansService implements ILoansService {

    private final LoansRepository loansRepository;
    @Override
    public void createLoans(String mobileNumber) {

        Optional<Loans> optionalLoans =  loansRepository.findByMobileNumber(mobileNumber);
        if (optionalLoans.isPresent()) {
            throw new LoanAlreadyExistsException("Loan already registered with given mobileNumber"+ mobileNumber);
        }
        loansRepository.save(createNewLoan(mobileNumber));

    }

    @Override
    public LoansDto fetchLoansData(String mobileNumber) {
       Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
               () -> new ResourceNotFoundException("Loans", "Mobile Number", mobileNumber)
       );
       return LoansMapper.mapToLoansDto(loans, new LoansDto());
    }

    @Override
    public boolean updateLoans(LoansDto loansDto) {
        Loans loans= loansRepository.findByLoanNumber(loansDto.getLoanNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Loans", "LoanNumber", loansDto.getLoanNumber())
        );
        LoansMapper.mapToLoansDto(loans, loansDto);
        loansRepository.save(loans);
        return true;
    }

    @Override
    public boolean deleteLoans(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loans", "Mobile Number", mobileNumber)
        );
        loansRepository.deleteById(loans.getLoanId());
        return true;
    }

    private Loans createNewLoan(String mobileNumber) {
        Loans newLoan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }
}
