package org.example.userproject1.validator;

public class Error {

    private final String code;
    private final String message;

    public static Error of(String code, String message){
        return new Error(code, message);
    }

    public Error(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}