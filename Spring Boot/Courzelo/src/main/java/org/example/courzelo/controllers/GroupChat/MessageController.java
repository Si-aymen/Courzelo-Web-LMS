package org.example.courzelo.controllers.GroupChat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.Groups.MessagesREQ;
import org.example.courzelo.models.GroupChat.Group;
import org.example.courzelo.models.GroupChat.Message;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.serviceImpls.Groups.GroupService;
import org.example.courzelo.serviceImpls.Groups.MessageService;
import org.example.courzelo.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private final MessageService messageService;
    private final GroupService groupService;
    private final UserRepository userRepository;

    @GetMapping("/group/{groupId}")
    public List<Message> getMessagesByGroupId(@PathVariable String groupId) {
        return messageService.getMessagesByGroupId(groupId);
    }

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody MessagesREQ messageReq) {
        // Validate request
        if (messageReq == null || messageReq.getSenderId() == null || messageReq.getGroupId() == null || messageReq.getText() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Find user and group
        User user = userRepository.findUserByEmail(messageReq.getSenderId());
        Group group = groupService.getGroupById(messageReq.getGroupId());

        // Check if user or group is not found
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Create and save message
        Message message = new Message();
        message.setText(messageReq.getText());
        message.setGroup(group);
        message.setSender(user);

        Message savedMessage = messageService.sendMessage(message);

        return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
    }
}
