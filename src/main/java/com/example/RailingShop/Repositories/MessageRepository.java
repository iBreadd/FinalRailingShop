package com.example.RailingShop.Repositories;

import com.example.RailingShop.Entities.Message;
import com.example.RailingShop.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllBySender(User sender);
    List<Message> findAllByReceiver(User receiver);
    List<Message> findAllByIsSupportResponse(boolean isSupportResponse);
}
