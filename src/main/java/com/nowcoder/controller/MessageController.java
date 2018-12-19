package com.nowcoder.controller;

import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 消息中心的控制层
 *
 * @author wangleifu
 * @create 2018-12-19 15:10
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/msg/detail"}, method={RequestMethod.GET})
    public String conversationDetail(@RequestParam("conversationId") String conversationId, Model model) {
        try {
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> messages = new ArrayList<>();
            for (Message message : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                User user = userService.getUser(message.getFromId());
                if (user == null) {
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userId", user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
        } catch (Exception e) {
            logger.error("获取站内信列表失败 " +e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/addMessage"}, method={RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        try {
            Message message = new Message();
            message.setFromId(fromId);
            message.setToId(toId);
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setHasRead(0);
            message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            messageService.addMessage(message);
            return ToutiaoUtil.getJSONString(0, String.valueOf(message.getId()));
        } catch(Exception e) {
            logger.error("站内消息发送失败 " + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "消息发送失败");
        }
    }
}
