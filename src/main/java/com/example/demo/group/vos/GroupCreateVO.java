package com.example.demo.group.vos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupCreateVO {
    @NotBlank
    @Size(min = 3, max = 40)
    private String name;
    @Size(max = 255)
    private String description;
}
