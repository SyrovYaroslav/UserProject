package org.example.userproject1.validator;

public interface Validator<T>{
    ValidationResult isValid(T object);
}
