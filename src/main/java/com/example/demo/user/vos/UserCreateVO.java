package com.example.demo.user.vos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCreateVO {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;
    @NotBlank
    private String email;
    @NotBlank
    private String phoneNumber;
    @NotBlank()
    @Pattern(regexp = "^.{8,}$", message = "Password must be at least 8 characters long!!!")
    private String password;
}
