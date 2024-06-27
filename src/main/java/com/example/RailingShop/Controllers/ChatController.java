package com.example.RailingShop.Controllers;

import com.example.RailingShop.Entities.Message;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Repositories.UserRepository;
import com.example.RailingShop.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/messages")
public class ChatController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageService messageService;

    @GetMapping("/send")
    public String showSendMessageForm(Model model) {
        model.addAttribute("message", new Message());
        return "sendMessage";
    }

    @PostMapping("/send")
    public String sendMessage(@ModelAttribute Message message, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User sender = userRepository.findByUsername(username);

        User receiver = userRepository.findByUsername("admin"); // Assuming messages are sent to a default admin
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setSupportResponse(false); // маркиране на съобщението като не-отговор от поддръжка
        messageService.saveMessage(message);

        redirectAttributes.addFlashAttribute("message", "Your message has been sent successfully!");
        return "redirect:/shop/home";
    }

    @GetMapping("/inbox")
    public String viewInbox(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        List<Message> messages = messageService.getMessagesByReceiver(user);
        model.addAttribute("messages", messages);
        return "inbox";
    }

    @GetMapping("/support")
    public String viewSupportInbox(Model model) {
        List<Message> messages = messageService.getAllSupportMessages(); // използване на новия метод
        model.addAttribute("messages", messages);
        return "supportInbox";
    }

    @PostMapping("/reply")
    public String replyMessage(@RequestParam Long messageId, @RequestParam String content,RedirectAttributes redirectAttributes) {
        Message originalMessage = messageService.findById(messageId);
        Message replyMessage = new Message();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String senderUsername = authentication.getName();
        User sender = userRepository.findByUsername(senderUsername);

        replyMessage.setSender(sender);
        replyMessage.setReceiver(originalMessage.getSender());
        replyMessage.setContent(content);
        replyMessage.setSupportResponse(true); // маркиране на съобщението като отговор от поддръжка
        messageService.saveMessage(replyMessage);
        redirectAttributes.addFlashAttribute("message", "You answered the question!");

        return "redirect:/messages/support";
    }

    @PostMapping("/delete")
    public String deleteMessage(@RequestParam Long messageId,RedirectAttributes redirectAttributes) {
        messageService.deleteMessage(messageId);
        redirectAttributes.addFlashAttribute("message", "Messages has been deleted!");
        return "redirect:/shop/home";
    }
}





