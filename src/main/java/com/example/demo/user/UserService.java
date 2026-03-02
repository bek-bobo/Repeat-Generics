package com.example.demo.user;


import com.example.demo.user.entity.User;
import com.example.demo.user.vos.UserCreateVO;
import com.example.demo.user.vos.UserResponseVO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
        private final UserRepository userRepository;
        private final ModelMapper modelMapper;


        public UserResponseVO create(UserCreateVO userCreateVO) {

                User user = modelMapper.map(userCreateVO, User.class);
                user.setId(UUID.randomUUID());
                User save = userRepository.save(user);
                return modelMapper.map(save, UserResponseVO.class);

        }
}
