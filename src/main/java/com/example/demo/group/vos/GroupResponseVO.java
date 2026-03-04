package com.example.demo.group.vos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupResponseVO {

    private UUID id;
    private String name;
    private String description;
}
