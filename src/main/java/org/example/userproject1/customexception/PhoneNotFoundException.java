package org.example.userproject1.customexception;

public class PhoneNotFoundException extends RuntimeException{
    public PhoneNotFoundException(String message){
        super(message);
    }
}
