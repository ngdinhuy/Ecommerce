package com.example.ecommerce.Resources;

import com.amazonaws.services.pinpoint.model.MessageResponse;
import com.example.ecommerce.model.MessageModel;
import com.example.ecommerce.model.User;
import com.example.ecommerce.response.BaseResponse;
import com.example.ecommerce.response.ListMessageResponse;
import com.example.ecommerce.response.PagingBaseResponse;
import com.example.ecommerce.service.MessageService;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.utils.Define;
import com.example.ecommerce.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/chat")
public class MessageResource {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @GetMapping("/list/{id_user}")
    ResponseEntity<BaseResponse> getListMessage(@PathVariable("id_user") Integer idUser) {
        User user = userService.findUserById(idUser);
        if (user == null) {
            return Utils.getResponse(Define.ERROR_CODE, new String[]{"User is not exist"}, null);
        }
        List<MessageModel> listMessage = messageService.getListMessage(user);
        List<ListMessageResponse> listMessageResponse = new ArrayList<>();
        for (MessageModel messageModel : listMessage) {
            if (idUser == messageModel.getSender().getId()) {
                ListMessageResponse messageResponse = new ListMessageResponse(
                        messageModel.getReciever().getId(),
                        messageModel.getReciever().getName(),
                        messageModel.getReciever().getAvatar(),
                        messageModel.getMessage(),
                        Utils.setUpDateChatList(messageModel.getDate()),
                        true,
                        0,
                        messageModel.getDate()
                );
                listMessageResponse.add(messageResponse);
            } else {
                Integer unread = messageService.getListMessageUnreadBySenderAndReceiver(messageModel.getSender(), user);
                ListMessageResponse messageResponse = new ListMessageResponse(
                        messageModel.getSender().getId(),
                        messageModel.getSender().getName(),
                        messageModel.getSender().getAvatar(),
                        messageModel.getMessage(),
                        Utils.setUpDateChatList(messageModel.getDate()),
                        false,
                        unread,
                        messageModel.getDate()
                );
                listMessageResponse.add(messageResponse);
            }
        }

//        Sap xep lai thu tu tin nhan
//        Nếu cùng lớn hơn 0 thì so sánh ngày tháng
//        Nếu 1 thằng = 0 và 1 thăngf khác 0 thì lấy thằng khác 0
//        Nếu cùng bằng 0 so sánh date
        listMessageResponse.sort(new Comparator<ListMessageResponse>() {
            @Override
            public int compare(ListMessageResponse o1, ListMessageResponse o2) {
                if (o1.getNumberUnreadMessage() > 0 && o2.getNumberUnreadMessage() > 0) {
                    return o2.getDate().compareTo(o1.getDate());
                }
                if (o2.getNumberUnreadMessage() != o1.getNumberUnreadMessage()) {
                    return o2.getNumberUnreadMessage() - o1.getNumberUnreadMessage();
                }
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, listMessageResponse);
    }

    @GetMapping("/message")
    ResponseEntity<PagingBaseResponse> gitListMessageUser(@RequestParam(name = "id_user") Integer idUser,
                                                          @RequestParam(name = "id_partner") Integer idPartner,
                                                          @RequestParam(name = "offset") Integer offset) {
        if (idUser == null || idPartner == null || offset == null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new PagingBaseResponse(HttpStatus.NOT_FOUND.value(), new String[]{"Please send enough infomation"}, null, 0)
            );
        }
        Page<MessageModel> response = messageService.getListMessage(offset, idUser, idPartner);
        int returnOffset = 0;
        List<MessageModel> listMessageModel = response.getContent();
        for (MessageModel messageModel : listMessageModel) {
            messageModel.setFromYou(messageModel.getSender().getId() == idUser);
            messageModel.setFormatDate(Utils.setUpDateDetail(messageModel.getDate()));
        }

        messageService.updateUnreadMessage(idPartner);

        ArrayList<MessageModel> listMessageResponse = new ArrayList<>(listMessageModel);
        Utils.reverseList(listMessageResponse);

        if (response.getContent().size() % Define.NUMBER_PER_PAGE == 0) {
            returnOffset = offset + Define.NUMBER_PER_PAGE;
        } else {
            returnOffset = offset + response.getContent().size();
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new PagingBaseResponse(HttpStatus.OK.value(), new String[]{}, listMessageResponse, returnOffset)
        );
    }

    @GetMapping("/full")
    ResponseEntity<BaseResponse> getFullMessageByUser(@RequestParam(name = "id_sender") Integer idSender,
                                                      @RequestParam(name = "id_receiver") Integer idReceiver,
                                                      @RequestParam(name = "offset") Integer offset) {
        if (idSender == null || idReceiver == null || offset == null) {
            return Utils.getResponse(HttpStatus.NOT_FOUND.value(), new String[]{"Please send enough infomation"}, null);
        }
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, messageService.getFullMessage(idSender, idReceiver));
    }

}
