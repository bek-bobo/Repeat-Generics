package com.example.demo.group.group.mapper;

import com.example.demo.core.coreModelMapper.CoreMapper;
import com.example.demo.group.entity.Group;
import com.example.demo.group.vos.GroupCreateVO;
import com.example.demo.group.vos.GroupResponseVO;
import com.example.demo.group.vos.GroupUpdateVO;
import com.example.demo.group.vos.GroupWithUsersResponseVO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupMapper extends CoreMapper<
        Group,
        GroupCreateVO,
        GroupResponseVO,
        GroupUpdateVO> {

    private final ModelMapper modelMapper;

    @Override
    public GroupResponseVO convertToResponseVO(Group group) {
        return modelMapper.map(group, GroupResponseVO.class);
    }

    @Override
    public Group fromCreateToEntity(GroupCreateVO groupCreateVO) {
        return modelMapper.map(groupCreateVO, Group.class);
    }

    @Override
    public Group fromUpdateToEntity(GroupUpdateVO updateVO, Group group) {
        return checkGroupUpdates(group, updateVO);
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

    public GroupWithUsersResponseVO toGroupWithUsersResponseVO(Group group) {
        return modelMapper.map(group, GroupWithUsersResponseVO.class);
    }

}
