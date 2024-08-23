package org.example.courzelo.config;

import org.example.courzelo.dto.requests.Groups.MessagesREQ;
import org.example.courzelo.models.GroupChat.Group;
import org.example.courzelo.models.GroupChat.Message;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.Groups.GroupMessageRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.serviceImpls.Groups.MessageService;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ConcurrentMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final GroupMessageRepository groupRepository;
    private final ObjectMapper objectMapper;

    // Constructor injection for MessageService
    public ChatWebSocketHandler(MessageService messageService,UserRepository userRepository,GroupMessageRepository groupRepository) {
       this.groupRepository=groupRepository;
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Optional: to avoid writing dates as timestamps
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        System.out.println("New session established: " + session.getId());
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        // Convert incoming message to Message object
        MessagesREQ message = objectMapper.readValue(textMessage.getPayload(), MessagesREQ.class);

        // Debugging statements
        System.out.println("Received message request: " + message);

        User user = userRepository.findUserByEmail(message.getSenderId());
        Optional<Group> optionalGroup = groupRepository.findById(message.getGroupId());

        if (user == null) {
            System.err.println("User not found: " + message.getSenderId());
            return;
        }

        if (optionalGroup.isEmpty()) {
            System.err.println("Group not found: " + message.getGroupId());
            return;
        }

        Message message1 = new Message();
        message1.setText(message.getText());
        message1.setSender(user);
        message1.setGroup(optionalGroup.get());

        // Save the message to the database
        Message savedMessage = messageService.sendMessage(message1);
        System.out.println("Saved message: " + savedMessage);

        // Broadcast the message to all other sessions
        String broadcastMessage = objectMapper.writeValueAsString(savedMessage);
        for (WebSocketSession s : sessions.values()) {
            if (s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage(broadcastMessage));
                } catch (IOException e) {
                    System.err.println("Error sending message to session " + s.getId() + ": " + e.getMessage());
                }
            }
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        System.out.println("Session closed: " + session.getId());
    }
}
