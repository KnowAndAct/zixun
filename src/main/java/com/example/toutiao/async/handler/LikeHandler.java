package com.example.toutiao.async.handler;

import com.example.toutiao.async.EventHandler;
import com.example.toutiao.async.EventModel;
import com.example.toutiao.async.EventType;
import com.example.toutiao.model.Message;
import com.example.toutiao.model.User;
import com.example.toutiao.service.MessageService;
import com.example.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandler(EventModel model) {
        //System.out.println("Liked");
        Message message = new Message();
        User user = userService.getUser(model.getActorId());
        message.setToId(model.getActorId());
        message.setFromId(3);
        message.setContent("用户" + user.getName() +
                " 赞了你的资讯,http://127.0.0.1:8080/news/"
                + String.valueOf(model.getEntityId()));
        message.setConversationId("0_3");
        message.setCreatedDate(new Date());
        //message.setConversationId();
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
