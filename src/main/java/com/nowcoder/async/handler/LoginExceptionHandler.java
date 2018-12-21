package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 登录异常 处理器
 *
 * @author wangleifu
 * @create 2018-12-21 14:32
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;


    @Override
    public void doHandler(EventModel model) {
        Message message = new Message();
        int fromId = 5;
        int toId = model.getActorId();
        message.setToId(toId);
        message.setFromId(fromId);
        StringBuilder sb = new StringBuilder();
        message.setContent(sb.append("尊敬的用户：").append(userService.getUser(toId).getName())
                .append(" , 您上次登录的IP异常，如非本人操作，请及时修改密码！").toString());
        message.setCreatedDate(new Date());
        message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
