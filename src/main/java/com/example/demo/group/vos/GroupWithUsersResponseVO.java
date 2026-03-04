package com.example.demo.group.vos;

import com.example.demo.user.vos.UserResponseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupWithUsersResponseVO extends GroupResponseVO {
    private List<UserResponseVO> users;
}
