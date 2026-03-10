package com.example.demo.user;


import com.example.demo.core.coreModelMapper.CoreMapper;
import com.example.demo.core.customExeptionHandler.UserNotFoundException;
import com.example.demo.core.repository.CoreRepository;
import com.example.demo.core.service.CoreService;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.UserStatus;
import com.example.demo.user.user.mapper.UserMapper;
import com.example.demo.user.vos.UserCreateVO;
import com.example.demo.user.vos.UserResponseVO;
import com.example.demo.user.vos.UserUpdateVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService extends CoreService<UUID, User, UserResponseVO, UserCreateVO, UserUpdateVO> {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    protected CoreRepository<User, UUID> getRepository() {
        return userRepository;
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected CoreMapper<User, UserCreateVO, UserResponseVO, UserUpdateVO> getCoreMapper() {
        return userMapper;
    }

    @Override
    protected UserResponseVO internalCreate(UserCreateVO userCreateVO) {
        User user = userMapper.fromCreateToEntity(userCreateVO);
        user.setUserStatus(UserStatus.WAITING);

        User saved = userRepository.save(user);
        return userMapper.convertToResponseVO(user);
    }

    @Override
    protected UserResponseVO internalUpdate(UUID uuid, UserUpdateVO userUpdateVO) {

        User updatedUser = userMapper.fromUpdateToEntity(userUpdateVO, getUserOrThrow(uuid));
        User saved = userRepository.save(updatedUser);

        return userMapper.convertToResponseVO(updatedUser);
    }


    private User getUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

}
