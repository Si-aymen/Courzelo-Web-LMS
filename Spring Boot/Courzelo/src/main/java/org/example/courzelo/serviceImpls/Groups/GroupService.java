package org.example.courzelo.serviceImpls.Groups;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.GroupChat.Group;
import org.example.courzelo.repositories.Groups.GroupMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {

    @Autowired
    private final GroupMessageRepository groupRepository;

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group getGroupById(String id) {
        return groupRepository.findById(id).orElse(null);
    }

    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    public List<String> getMembersByGroup(String id) {
        // Find the group by its ID
        Optional<Group> groupOptional = groupRepository.findById(id);

        // If the group is found, return the list of member IDs
        if (groupOptional.isPresent()) {
            return groupOptional.get().getMembers();
        }

        // If the group is not found, return an empty list or throw an exception
        return Collections.emptyList(); // or throw new GroupNotFoundException("Group not found");
    }}