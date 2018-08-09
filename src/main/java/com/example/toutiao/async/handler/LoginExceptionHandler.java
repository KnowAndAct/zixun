package com.example.toutiao.async.handler;

import com.example.toutiao.async.EventHandler;
import com.example.toutiao.async.EventModel;
import com.example.toutiao.async.EventType;
import com.example.toutiao.model.Message;
import com.example.toutiao.service.MessageService;
import com.example.toutiao.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandler(EventModel model) {
        //判断是否异常登录
        //System.out.println("123456");
        Message message = new Message();
        message.setToId(model.getActorId());
        message.setContent("你上次登录的IP异常");
        message.setFromId(3);
        message.setCreatedDate(new Date());
        message.setConversationId("0_3");
        messageService.addMessage(message);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("mail"), "你妹的，害我忙活了一晚上", "mails/welcome.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
