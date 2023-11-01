package com.example.ecommerce.service;

import com.example.ecommerce.model.MessageModel;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.MessageRepository;
import com.example.ecommerce.request.Message;
import com.example.ecommerce.utils.Define;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageRepository repository;

   public MessageModel addMessaggeToDB(MessageModel message){
        return repository.save(message);
    }

    public List<MessageModel> getListMessage(User user){
        return repository.findListLateMessage(user.getId());
    }

    public Integer getListMessageUnreadBySenderAndReceiver(User sender, User receiver){
       return repository.findMessageModelsBySenderAndRecieverAndReadStatus(sender, receiver, Define.StatusReadMessage.UNREAD).size();
    }

    public Page<MessageModel> getListMessage(Integer offset, Integer idSender, Integer idReceiver){
       return repository.getMessageModelBySenderAndReceiver(PageRequest.of(offset/Define.NUMBER_PER_PAGE, Define.NUMBER_PER_PAGE), idSender, idReceiver);
    }

    public List<MessageModel> getFullMessage(Integer idSender, Integer idReceiver){
        return repository.getMessageModelBySenderAndReceiver(idSender, idReceiver);
    }

    public void updateUnreadMessage(Integer idSender){
       repository.updateMessageRead(idSender);
    }
}
