package com.example.demo.core.customExeptionHandler;




import java.util.UUID;


public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException(UUID id) {
        super("Group not found with id: " + id);
    }


}
