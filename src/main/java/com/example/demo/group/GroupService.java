package com.example.demo.group;


import com.example.demo.core.coreModelMapper.CoreMapper;
import com.example.demo.core.customExeptionHandler.BusinessRuleException;
import com.example.demo.core.customExeptionHandler.GroupNotFoundException;
import com.example.demo.core.customExeptionHandler.UserNotFoundException;
import com.example.demo.core.repository.CoreRepository;
import com.example.demo.core.service.CoreService;
import com.example.demo.group.entity.Group;
import com.example.demo.group.entity.GroupStatus;
import com.example.demo.group.mapper.GroupMapper;
import com.example.demo.group.repository.GroupRepository;
import com.example.demo.group.vos.*;
import com.example.demo.rsql.SpecificationBuilder;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class GroupService extends CoreService<UUID, Group, GroupResponseVO, GroupCreateVO, GroupUpdateVO> {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMapper groupMapper;

    @Override
    protected CoreMapper<Group, GroupCreateVO, GroupResponseVO, GroupUpdateVO> getCoreMapper() {
        return groupMapper;
    }

    @Override
    protected CoreRepository<Group, UUID> getRepository() {
        return groupRepository;
    }

    @Override
    protected Class<Group> getEntityClass() {
        return Group.class;
    }


    @Override
    protected GroupResponseVO internalCreate(GroupCreateVO groupCreateVO) {
        Group group = groupMapper.fromCreateToEntity(groupCreateVO);

        group.setGroupStatus(GroupStatus.PLANNED);

        Group saved = groupRepository.save(group);

        return groupMapper.convertToResponseVO(saved);
    }

    @Override
    protected GroupResponseVO internalUpdate(UUID uuid, GroupUpdateVO updateVO) {
        Group entity = getGroupOrThrow(uuid);

        Group group = groupMapper.fromUpdateToEntity(updateVO, entity);
        Group saved = groupRepository.save(group);

        return groupMapper.convertToResponseVO(saved);
    }

    @Override
    protected RuntimeException notFoundException(UUID uuid) {
        return new GroupNotFoundException(uuid);
    }

    @Transactional
    public GroupWithUsersResponseVO addUserToGroup(AddUserToGroupVO addUserToGroupVO) {

        Group group = getGroupOrThrow(addUserToGroupVO.getGroupId());
        User user = getUserOrThrow(addUserToGroupVO.getUserId());

        validateUserCanBeAdded(group, user);

        user.setUserStatus(UserStatus.ACTIVE);
        group.addUser(user);

        return groupMapper.toGroupWithUsersResponseVO(group);
    }

    @Transactional(readOnly = true)
    public GroupWithUsersResponseVO getGroupWithUsers(UUID id) {
        return groupMapper.toGroupWithUsersResponseVO(getGroupOrThrow(id));
    }

    @Transactional
    public void removeUserFromGroup(DeleteUserFromGroup deleteUserFromGroup) {

        Group group = getGroupOrThrow(deleteUserFromGroup.getGroupId());
        User user = getUserOrThrow(deleteUserFromGroup.getUserId());

        if (!group.getUsers().contains(user)) {
            throw new BusinessRuleException("User with this %s is not in this Group %s".formatted(user.getId(), group.getId()));
        }

        group.removeUser(user);
        user.setUserStatus(UserStatus.WAITING);
    }

    @Transactional
    public void cancelGroup(UUID groupId) {

        Group group = getGroupOrThrow(groupId);

        group.getUsers().forEach(user -> {
            user.setGroup(null);
            user.setUserStatus(UserStatus.WAITING);
        });

        groupRepository.delete(group);
    }

    @Transactional(readOnly = true)
    public Page<GroupWithUsersResponseVO> getAllGroupWithUsers(String predicate, Pageable pageable) {

        Specification<Group> specification = SpecificationBuilder.build(predicate);
        Page<Group> pages;

        if (specification == null) {
            pages = groupRepository.findAll(pageable);
        } else {
            pages = groupRepository.findAll(specification, pageable);
        }

        return pages.map(groupMapper::toGroupWithUsersResponseVO);
    }

    private Group getGroupOrThrow(UUID groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> notFoundException(groupId));
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
