package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.MailSender;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

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

    @Autowired
    private MailSender mailSender;


    @Override
    public void doHandler(EventModel model) {
        Message message = new Message();
        int fromId = ToutiaoUtil.SYS_ADMIN_ID;
        int toId = model.getActorId();
        message.setToId(toId);
        message.setFromId(fromId);
        StringBuilder sb = new StringBuilder();
        message.setContent(sb.append("尊敬的用户：").append(userService.getUser(toId).getName())
                .append(" , 您上次登录的IP异常，如非本人操作，请及时修改密码！").toString());
        message.setCreatedDate(new Date());
        message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
        messageService.addMessage(message);

        // 发送邮件
        Map<String, Object> map = new HashMap<>(1);
        map.put("username", model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("to"), "登陆异常",
                "mails/loginException.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
