package com.example.demo.user.vos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateVO {

    @Size(min = 3, max = 50)
    private String username;
    @Email
    private String email;

    private String phoneNumber;

    @Size(min = 3, max = 50)
    private String password;






}
