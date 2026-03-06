package com.example.demo.user;


import com.example.demo.core.customExeptionHandler.UserNotFoundException;
import com.example.demo.rsql.SpecificationBuilder;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.UserStatus;
import com.example.demo.user.vos.UserCreateVO;
import com.example.demo.user.vos.UserResponseVO;
import com.example.demo.user.vos.UserUpdateVO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Transactional
    public UserResponseVO create(UserCreateVO userCreateVO) {
        User user = new User();

        user.setUsername(userCreateVO.getUsername());
        user.setPassword(userCreateVO.getPassword());
        user.setEmail(userCreateVO.getEmail());
        user.setPhoneNumber(userCreateVO.getPhoneNumber());
        user.setUserStatus(UserStatus.WAITING);

        User saved = userRepository.save(user);
        return modelMapper.map(saved, UserResponseVO.class);

    }

    @Transactional
    public Page<UserResponseVO> getAllUsers(String predicate, Pageable pageable) {

        return userRepository.findAll(SpecificationBuilder.build(predicate),pageable)
                .map(user -> modelMapper.map(user, UserResponseVO.class));
    }

    @Transactional
    public UserResponseVO getUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return modelMapper.map(user, UserResponseVO.class);

    }

    @Transactional
    public UserResponseVO updateUser(UUID id, UserUpdateVO vo) {

        User updatedUser = checkUserData(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)), vo);

        User saved = userRepository.save(updatedUser);
        return modelMapper.map(saved, UserResponseVO.class);

    }

    @Transactional
    public void deleteUser(UUID id) {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.deleteById(id);
    }

    private User checkUserData(User user, UserUpdateVO vo) {

        if (vo.getUsername() != null) {
            user.setUsername(vo.getUsername());
        }
        if (vo.getPassword() != null) {
            user.setPassword(vo.getPassword());
        }
        if (vo.getEmail() != null) {
            user.setEmail(vo.getEmail());
        }
        if (vo.getPhoneNumber() != null) {
            user.setPhoneNumber(vo.getPhoneNumber());
        }
        return user;
    }
}
