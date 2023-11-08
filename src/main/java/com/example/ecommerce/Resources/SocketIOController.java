package com.example.ecommerce.Resources;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.ecommerce.model.MessageModel;
import com.example.ecommerce.model.User;
import com.example.ecommerce.request.Message;
import com.example.ecommerce.service.MessageService;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.utils.Utils;
import lombok.extern.log4j.Log4j2;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class SocketIOController {
    @Autowired
    private SocketIOServer socketServer;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    SocketIOController(SocketIOServer socketServer) {
        this.socketServer = socketServer;

        this.socketServer.addConnectListener(onUserConnectWithSocket);
        this.socketServer.addDisconnectListener(onUserDisconnectWithSocket);
        this.socketServer.addEventListener("messageSendToUser", Message.class, onSendMessage);
        this.socketServer.addEventListener("get_message", Message.class, onChatReceived());
        this.socketServer.addEventListener("test", String.class, onChat);
    }


    public ConnectListener onUserConnectWithSocket = new ConnectListener() {
        @Override
        public void onConnect(SocketIOClient client) {
            String room = client.getHandshakeData().getSingleUrlParam("room");
            String idUser = client.getHandshakeData().getSingleUrlParam("id_user");
            // Join room để chat
            if (room != null) {
                client.joinRoom(room);
            }
            // Set sự kiện online khi vào app
            if (idUser != null) {

            }
            log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
        }
    };


    public DisconnectListener onUserDisconnectWithSocket = new DisconnectListener() {
        @Override
        public void onDisconnect(SocketIOClient client) {
            log.info("Perform operation on user disconnect in controller");
        }
    };

    public DataListener<Message> onSendMessage = new DataListener<Message>() {
        @Override
        public void onData(SocketIOClient client, Message message, AckRequest acknowledge) throws Exception {
            log.info(message.getSenderName() + " user send message to user " + message.getTargetUserName() + " and message is " + message.getMessage());
            User sender = userService.findUserById(Integer.parseInt(message.getSenderName()));
            User reciever = userService.findUserById(Integer.parseInt(message.getTargetUserName()));
            if (sender == null || reciever == null) {
                return;
            }
            MessageModel messageModel = new MessageModel(message.getMessage(), Utils.getCurrentDateTime(), sender, reciever, 0);
            try {
//                messageService.addMessaggeToDB(messageModel);
            } catch (Exception e) {
                log.info(e.getMessage());
            }
            socketServer.getBroadcastOperations().sendEvent(message.getTargetUserName(), client, message);


            /**
             * After sending message to target user we can send acknowledge to sender
             */
            acknowledge.sendAckData("Message send to target user successfully");
        }
    };

    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            //them ti nhan vao db
            User sender = userService.findUserById(Integer.parseInt(data.getSenderName()));
            User reciever = userService.findUserById(Integer.parseInt(data.getTargetUserName()));
            if (sender == null || reciever == null) {
                return;
            }
            MessageModel messageModel = new MessageModel(data.getMessage(), Utils.getCurrentDateTime(), sender, reciever, 0);
            try {
                messageService.addMessaggeToDB(messageModel);
            } catch (Exception e) {
                log.info(e.getMessage());
            }

            //push notification to user
            for (
                    SocketIOClient client : senderClient.getNamespace().getRoomOperations(data.getRoom()).getClients()) {
                client.sendEvent("get_message",
                        new Message(data.getSenderName(), data.getTargetUserName(), data.getMessage(), data.getRoom()));
            }
        };
    }

    public DataListener<String> onChat = new DataListener<String>() {
        @Override
        public void onData(SocketIOClient socketIOClient, String message, AckRequest ackRequest) throws Exception {
            log.info(message);
        }
    };

}
