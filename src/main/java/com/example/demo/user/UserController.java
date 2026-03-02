package com.example.demo.user;


import com.example.demo.user.vos.UserCreateVO;
import com.example.demo.user.vos.UserResponseVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    private ResponseEntity<UserResponseVO> createUser(@Valid @RequestBody UserCreateVO userCreateVO){

       UserResponseVO userResponseVO =  userService.create(userCreateVO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userResponseVO);
    }

}
