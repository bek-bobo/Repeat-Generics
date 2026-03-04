package com.example.demo.user;


import com.example.demo.user.vos.UserCreateVO;
import com.example.demo.user.vos.UserResponseVO;
import com.example.demo.user.vos.UserUpdateVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponseVO> createUser(@Valid @RequestBody UserCreateVO userCreateVO) {

        UserResponseVO userResponseVO = userService.create(userCreateVO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userResponseVO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseVO>> getAllUser() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseVO> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<UserResponseVO> updateUser(
            @PathVariable UUID id,
            @RequestBody UserUpdateVO vo) {

        return ResponseEntity.ok(userService.updateUser(id, vo));
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<Map<String,String>> deleteUser(@PathVariable UUID id) {

        userService.deleteUser(id);
        return ResponseEntity.ok(
                Map.of("message", "User deleted successfully")
        );
    }

}
