package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.QiniuService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 资讯相关，上传图片
 *
 * @author wangleifu
 * @create 2018-12-17 14:35
 */
@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    UserService userService;

    @Autowired
    NewsService newsService;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    QiniuService qiniuService;


    /**
     * 上传图片
     * @param file 图片文件
     * @return
     */
    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file")MultipartFile file) {
        try {
            /*String fileURL = newsService.saveImage(file);*/
            String fileURL = qiniuService.saveImage(file);
            if (fileURL == null) {
                return ToutiaoUtil.getJSONString(1, "上传图片失败！");
            }
            return ToutiaoUtil.getJSONString(0, fileURL);
        } catch (Exception e) {
            logger.error("上传图片失败！" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "上传失败！");
        }
    }

    /**
     * 获取图片
     * @param imgName  要获取的图片链接
     * @param response 将图片copy到response中，传到前端
     */
    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imgName,
                         HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMG_DIR + imgName)),
                    response.getOutputStream());
        } catch (Exception e) {
            logger.error("读取图片失败" + e.getMessage());
        }
    }

    /**
     * 增加咨询信息
     * @param image 图片连接
     * @param title 标题
     * @param link  内容链接
     * @return
     */
    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link")  String link) {
        try {
            News news = new News();
            if (hostHolder.getUser() != null)
            {
                news.setUserId(hostHolder.getUser().getId());
            } else {
                // 匿名ID
                news.setUserId(3);
            }
            news.setImage(image);
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setLink(link);
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("添加资讯错误" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "发布失败！");
        }
    }

    /**
     * 跳转到对应咨询的详情页面
     * @param newsId 咨询Id
     * @return
     */
    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model) {
        News news = newsService.getById(newsId);
        if(news != null) {
            List<Comment> comments = commentService.getCommentsByEntity(newsId, EntityType.ENTITY_NEWS);
            List<ViewObject> commentVOS = new ArrayList<>();
            for (Comment comment : comments) {
                ViewObject vo = new ViewObject();
                vo.set("comment", comment);
                vo.set("user", userService.getUser(comment.getUserId()));
                commentVOS.add(vo);
            }
            model.addAttribute("comments", commentVOS);
        }
        model.addAttribute("news", news);
        model.addAttribute("owner", userService.getUser(news.getUserId()));
        return "detail";
    }

    /**
     *  增加一条评论
     * @param newsId  被评论的资讯id
     * @param content 增加的评论的内容
     * @return
     */
    @RequestMapping(path={"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content) {
        try {
            content = HtmlUtils.htmlEscape(content);
            // 过滤content
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setUserId(hostHolder.getUser().getId());
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);

            // 更新news的评论数量，以后用异步实现
            int count = commentService.getCommentCount(newsId, EntityType.ENTITY_NEWS);
            newsService.updateCommentCount(comment.getEntityId(), count);
        } catch (Exception e) {
            logger.error("添加评论失败 " + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }
}
