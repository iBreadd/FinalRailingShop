package com.example.RailingShop;

import com.example.RailingShop.Entities.Message;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Repositories.MessageRepository;
import com.example.RailingShop.Services.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveMessage() {
        // Mock data
        Message message = new Message();
        message.setContent("Hello");
        message.setSender(new User(1L));
        message.setReceiver(new User(2L));

        // Call service method
        messageService.saveMessage(message);

        // Verify that message was saved with timestamp set
        assertNotNull(message.getTimestamp());
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    void testDeleteMessage() {
        // Mock data
        Long messageId = 1L;

        // Call service method
        messageService.deleteMessage(messageId);

        // Verify that deleteById method of repository was called
        verify(messageRepository, times(1)).deleteById(messageId);
    }

    @Test
    void testFindById() {
        // Mock data
        Long messageId = 1L;
        Message message = new Message(messageId, new User(1L), new User(2L), "Hello", LocalDateTime.now(), false);
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        // Call service method
        Message foundMessage = messageService.findById(messageId);

        // Assertions
        assertNotNull(foundMessage);
        assertEquals(message.getId(), foundMessage.getId());
        assertEquals(message.getContent(), foundMessage.getContent());
        assertEquals(message.getSender(), foundMessage.getSender());
        assertEquals(message.getReceiver(), foundMessage.getReceiver());
        assertEquals(message.getTimestamp(), foundMessage.getTimestamp());
        assertEquals(message.isSupportResponse(), foundMessage.isSupportResponse());
    }

    @Test
    void testGetAllMessages() {
        // Mock data
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(1L, new User(1L), new User(2L), "Message 1", LocalDateTime.now(), false));
        messages.add(new Message(2L, new User(2L), new User(1L), "Message 2", LocalDateTime.now(), true));
        when(messageRepository.findAll()).thenReturn(messages);

        // Call service method
        List<Message> foundMessages = messageService.getAllMessages();

        // Assertions
        assertNotNull(foundMessages);
        assertEquals(2, foundMessages.size());
        assertEquals(messages.get(0), foundMessages.get(0));
        assertEquals(messages.get(1), foundMessages.get(1));
    }

}