package org.example.courzelo.controllers.GroupChat;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.Groups.GroupsREQ;
import org.example.courzelo.dto.requests.PostREQ;
import org.example.courzelo.models.Forum.Post;
import org.example.courzelo.models.Forum.SubForum;
import org.example.courzelo.models.GroupChat.Group;
import org.example.courzelo.models.User;
import org.example.courzelo.serviceImpls.Groups.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/groups")
public class ChatGroupController {

    @Autowired
    private final GroupService groupService;

    @GetMapping("all")
    public List<Group> getAllGroups() {
        return groupService.getAllGroups();
    }

    @PutMapping("addmember")
    public ResponseEntity<Object> addmember(@RequestBody GroupsREQ group) {
        try {
            Group group1 = groupService.getGroupById(group.getId());
            group1.getMembers().add(group.getMembers());
            return ResponseEntity.ok().body("{\"message\": \"member ajouté avec succès!\"}"); // Return JSON object
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Échec de l'ajout du member: " + e.getMessage() + "\"}"); // Return JSON object
        }
    }

    @PutMapping("addmember1")
    public ResponseEntity<Object> addMember1(@RequestBody GroupsREQ groupReq) {
        try {
            Group group = groupService.getGroupById(groupReq.getId());
            if (group == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"error\": \"Group not found\"}");
            }

            // Adding members (assuming they are in the correct format)
            List<String> members = group.getMembers();
            if (members == null) {
                members = new ArrayList<>();
            }
            members.add(groupReq.getMembers());
            group.setMembers(members);
            groupService.createGroup(group); // Save the updated group

            return ResponseEntity.ok().body("{\"message\": \"Member added successfully!\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to add member: " + e.getMessage() + "\"}");
        }
    }


    @DeleteMapping("deletemember")
    public ResponseEntity<Object> deletemember(@RequestBody GroupsREQ group) {
        try {
            Group group1 = groupService.getGroupById(group.getId());
            group1.getMembers().remove(group.getMembers());
            return ResponseEntity.ok().body("{\"message\": \"member delete avec succès!\"}"); // Return JSON object
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Échec de l'delete du member: " + e.getMessage() + "\"}"); // Return JSON object
        }
    }

    @GetMapping("/get/{id}")
    public Group getGroupById(@PathVariable String id) {
        return groupService.getGroupById(id);
    }

    @PostMapping("add")
    public Group createGroup(@RequestBody Group group) {
        return groupService.createGroup(group);
    }

    @GetMapping("/{id}/members")
    public List<String> getGroupMembers(@PathVariable String id) {
        return groupService.getMembersByGroup(id);
    }
}
