package com.example.demo.core.customExeptionHandler;



import java.util.UUID;


public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(UUID id) {
        super("User not found with id: " + id);
    }

}
