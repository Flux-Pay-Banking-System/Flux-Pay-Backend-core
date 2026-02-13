package com.bank.loan.controller;


import com.bank.loan.constants.LoansConstants;
import com.bank.loan.dto.LoansDto;
import com.bank.loan.dto.ResponseDto;
import com.bank.loan.service.impl.LoansService;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class LoansController {

    private final LoansService loansService;
    @PostMapping("/create")
    public ResponseEntity<ResponseDto>  createLoansDto(@RequestParam
                                                           @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                           String mobileNumber) {
       loansService.createLoans(mobileNumber);
       return ResponseEntity
               .status(HttpStatus.CREATED)
               .body(new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201));
    }
}
