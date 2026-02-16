package com.bank.card.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CardsAlreadyExistException extends RuntimeException{
    public CardsAlreadyExistException(String message){
        super(message);
    }
}
