package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 回复评论 处理器
 *
 * @author wangleifu
 * @create 2018-12-24 13:54
 */
@Component
public class CommentHandler implements EventHandler {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private NewsService newsService;


    @Override
    public void doHandler(EventModel model) {
        Message message = new Message();
        int fromId = ToutiaoUtil.SYS_ADMIN_ID;
        int toId = model.getEntityOwnerId();
        message.setFromId(fromId);
        message.setToId(toId);
        message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
        message.setCreatedDate(new Date());
        StringBuilder sb = new StringBuilder();
        User user = userService.getUser(model.getActorId());
        message.setContent(sb.append("用户").append(user.getName()).append("评论了你的资讯：")
                .append(newsService.getById(model.getEntityId()).getLink()).toString());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.COMMENT);
    }
}
