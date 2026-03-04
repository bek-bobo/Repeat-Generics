package com.example.demo.group.vos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddUserToGroupVO {
    private UUID groupId;
    private UUID userId;
}
