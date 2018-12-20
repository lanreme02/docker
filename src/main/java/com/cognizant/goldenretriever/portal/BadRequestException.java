package com.cognizant.goldenretriever.portal;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason = "")
final class BadRequestException extends RuntimeException{

    public BadRequestException(){

        super("Bad Request made");
    }

}
