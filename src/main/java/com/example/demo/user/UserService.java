package com.example.demo.user;


import com.example.demo.customExeptionHandler.UserNotFoundException;
import com.example.demo.user.entity.User;
import com.example.demo.user.vos.UserCreateVO;
import com.example.demo.user.vos.UserResponseVO;
import com.example.demo.user.vos.UserUpdateVO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Transactional
    public UserResponseVO create(UserCreateVO userCreateVO) {

        User user = modelMapper.map(userCreateVO, User.class);
        user.setId(UUID.randomUUID());
        User save = userRepository.save(user);
        return modelMapper.map(save, UserResponseVO.class);

    }

    @Transactional
    public List<UserResponseVO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponseVO.class))
                .toList();
    }

    @Transactional
    public UserResponseVO getUsers(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return modelMapper.map(user, UserResponseVO.class);

    }

    @Transactional
    public UserResponseVO updateUser(UUID id, UserUpdateVO vo) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (vo.getUsername() != null){
            user.setUsername(vo.getUsername());
        }
        if (vo.getPassword() != null){
            user.setPassword(vo.getPassword());
        }
        if (vo.getEmail() != null){
            user.setEmail(vo.getEmail());
        }
        if (vo.getPhoneNumber() != null){
            user.setPhoneNumber(vo.getPhoneNumber());
        }
        User saved = userRepository.save(user);
        return modelMapper.map(saved, UserResponseVO.class);

    }
    @Transactional
    public void deleteUser(UUID id) {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.deleteById(id);
    }
}
