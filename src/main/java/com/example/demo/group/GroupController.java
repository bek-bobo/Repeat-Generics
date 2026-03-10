package com.example.demo.group;

import com.example.demo.group.vos.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {
    private final GroupService groupService;


    @PostMapping("/create")
    public ResponseEntity<GroupResponseVO> createGroup(@Valid @RequestBody GroupCreateVO groupCreateVO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(groupService.internalCreate(groupCreateVO));
    }

    @PostMapping("/add-user")
    public ResponseEntity<GroupWithUsersResponseVO> addUserToGroup(
            @Valid
            @RequestBody AddUserToGroupVO addUserToGroupVO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(groupService.addUserToGroup(addUserToGroupVO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponseVO> getGroup(@PathVariable UUID id) {
        return ResponseEntity.ok(groupService.getById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<GroupResponseVO>> getAllGroup(
            @RequestParam(required = false) String predicate, Pageable pageable) {

        return ResponseEntity.ok(groupService.getAll(predicate, pageable));
    }


    @GetMapping("/{id}/users")
    public ResponseEntity<GroupWithUsersResponseVO> getGroupWithUsers(@PathVariable UUID id) {
        return ResponseEntity.ok(groupService.getGroupWithUsers(id));
    }

    @GetMapping("/all/users")
    public ResponseEntity<Page<GroupWithUsersResponseVO>> getAllGroupWithUsers(
            @RequestParam(required = false) String predicate, Pageable pageable) {

        return ResponseEntity.ok(groupService.getAllGroupWithUsers(predicate,pageable));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<GroupResponseVO> updateGroup(
            @PathVariable UUID id,
            @RequestBody GroupUpdateVO updateVO) {
        return ResponseEntity.ok(groupService.internalUpdate(id, updateVO));
    }

    @DeleteMapping("/delete/user/")
    public ResponseEntity<ActionResponse> deleteUser(
            @RequestBody DeleteUserFromGroup deleteUserFromGroup) {

        groupService.removeUserFromGroup(deleteUserFromGroup);

        return ResponseEntity.ok(new ActionResponse("User deleted from " + deleteUserFromGroup.getGroupId() + " successfully"));
    }

    @DeleteMapping("/{groupId}/cancel")
    public ResponseEntity<ActionResponse> cancelGroup(
            @PathVariable UUID groupId) {

        groupService.cancelGroup(groupId);
        return ResponseEntity.ok(
                new ActionResponse("Group cancelled successfully")
        );
    }

}
