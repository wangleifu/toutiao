package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.NewsService;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 点赞、点踩 逻辑控制器
 *
 * @author wangleifu
 * @create 2018-12-20 13:19
 */
@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@Param("newsId") int newsId) {
        try {
            int userId = hostHolder.getUser().getId();
            long likeCount = likeService.like(userId, EntityType.ENTITY_NEWS, newsId);
            newsService.updateLikeCount(newsId, (int)likeCount);

            eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(userId).setEntityOwnerId(newsService.getById(newsId).getUserId())
                    .setEntityType(EntityType.ENTITY_NEWS).setEntityId(newsId));

            return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
        } catch (Exception e) {
            logger.error("点赞异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "点赞异常");
        }
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(@Param("newsId") int newsId) {
        try {
            int userId = hostHolder.getUser().getId();
            long likeCount = likeService.dislike(userId, EntityType.ENTITY_NEWS, newsId);
            newsService.updateLikeCount(newsId, (int)likeCount);
            return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
        } catch (Exception e) {
            logger.error("点踩异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "点踩异常");
        }

    }

}
