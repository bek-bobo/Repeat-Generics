package com.example.demo.user.user.mapper;

import com.example.demo.core.coreModelMapper.CoreMapper;
import com.example.demo.user.entity.User;
import com.example.demo.user.vos.UserCreateVO;
import com.example.demo.user.vos.UserResponseVO;
import com.example.demo.user.vos.UserUpdateVO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper extends CoreMapper<
        User,
        UserCreateVO,
        UserResponseVO,
        UserUpdateVO> {
    private final ModelMapper modelMapper;

    @Override
    public UserResponseVO convertToResponseVO(User user) {
        return modelMapper.map(user, UserResponseVO.class);
    }

    @Override
    public User fromCreateToEntity(UserCreateVO userCreateVO) {
        return modelMapper.map(userCreateVO, User.class);
    }

    @Override
    public User fromUpdateToEntity(UserUpdateVO userUpdateVO, User user) {
        return checkUserData(user, userUpdateVO);
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
