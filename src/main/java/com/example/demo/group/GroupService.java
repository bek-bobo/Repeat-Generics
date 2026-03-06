package com.example.demo.group;


import com.example.demo.core.customExeptionHandler.BusinessRuleException;
import com.example.demo.core.customExeptionHandler.GroupNotFoundException;
import com.example.demo.core.customExeptionHandler.UserNotFoundException;
import com.example.demo.group.entity.Group;
import com.example.demo.group.entity.GroupStatus;
import com.example.demo.group.vos.*;

import com.example.demo.user.UserRepository;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.UserStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;


    @Transactional
    public GroupResponseVO createGroup(GroupCreateVO groupCreateVO) {

        Group group = new Group();
        group.setName(groupCreateVO.getName());
        group.setDescription(groupCreateVO.getDescription());
        group.setGroupStatus(GroupStatus.PLANNED);

        Group saved = groupRepository.save(group);
        return modelMapper.map(saved, GroupResponseVO.class);
    }

    @Transactional
    public GroupWithUsersResponseVO addUserToGroup(AddUserToGroupVO addUserToGroupVO) {

        Group group = getGroupOrThrow(addUserToGroupVO.getGroupId());
        User user = getUserOrThrow(addUserToGroupVO.getUserId());

        validateUserCanBeAdded(group, user);

        user.setUserStatus(UserStatus.ACTIVE);
        group.addUser(user);

        return modelMapper.map(group, GroupWithUsersResponseVO.class);
    }

    @Transactional
    public GroupResponseVO getGroup(UUID id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException(id));
        return modelMapper.map(group, GroupResponseVO.class);
    }

    @Transactional
    public GroupWithUsersResponseVO getGroupWithUsers(UUID id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException(id));
        return modelMapper.map(group, GroupWithUsersResponseVO.class);
    }

    @Transactional
    public GroupResponseVO updateGroup(UUID id, GroupUpdateVO updateVO) {
        Group group = checkGroupUpdates(getGroupOrThrow(id), updateVO);

        Group saved = groupRepository.save(group);
        return modelMapper.map(saved, GroupResponseVO.class);
    }

    @Transactional
    public void removeUserFromGroup(DeleteUserFromGroup deleteUserFromGroup) {

        Group group = getGroupOrThrow(deleteUserFromGroup.getGroupId());
        User user = getUserOrThrow(deleteUserFromGroup.getUserId());

        if (!group.getUsers().contains(user)) {
            throw new IllegalStateException("User is not in this group");
        }

        group.removeUser(user);
        user.setUserStatus(UserStatus.WAITING);
    }

    @Transactional
    public void cancelGroup(UUID groupId) {

        Group group = getGroupOrThrow(groupId);

        for (User user : group.getUsers()) {
            user.setGroup(null);
            user.setUserStatus(UserStatus.WAITING);
        }

        group.getUsers().clear();
        groupRepository.delete(group);
    }

    @Transactional
    public List<GroupResponseVO> getAllGroup() {
        return groupRepository.findAll()
                .stream()
                .map(group -> modelMapper.map(group, GroupResponseVO.class))
                .toList();

    }

    @Transactional
    public List<GroupWithUsersResponseVO> getAllGroupWithUsers() {
        return groupRepository.findAll()
                .stream()
                .map(group -> modelMapper.map(group, GroupWithUsersResponseVO.class))
                .toList();
    }


    private Group checkGroupUpdates(Group group, GroupUpdateVO updateVO) {
        if (updateVO.getName() != null) {
            group.setName(updateVO.getName());
        }
        if (updateVO.getDescription() != null) {
            group.setDescription(updateVO.getDescription());
        }

        return group;
    }

    private Group getGroupOrThrow(UUID groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));
    }

    private User getUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private void validateUserCanBeAdded(Group group, User user) {

        if (user.getGroup() != null) {
            throw new BusinessRuleException("User already assigned to a group");
        }

        if (group.getGroupStatus() != GroupStatus.PLANNED) {
            throw new BusinessRuleException("Cannot add user to started group");
        }

        if (group.getUsers().size() >= group.getMaxUsers()) {
            throw new BusinessRuleException("Group is full");
        }
    }

}
