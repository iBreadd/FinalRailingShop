package com.example.RailingShop.Services;

import com.example.RailingShop.Entities.Message;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getMessagesBySender(User sender) {
        return messageRepository.findAllBySender(sender);
    }

    public List<Message> getMessagesByReceiver(User receiver) {
        return messageRepository.findAllByReceiver(receiver);
    }

    public void saveMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    public Message findById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
    public List<Message> getAllSupportMessages() {
        return messageRepository.findAllByIsSupportResponse(false); // само съобщенията, които не са отговори от поддръжка
    }
}

