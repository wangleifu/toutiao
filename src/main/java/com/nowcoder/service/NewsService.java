package com.nowcoder.service;

import com.nowcoder.dao.NewsDAO;
import com.nowcoder.model.News;
import com.nowcoder.util.ToutiaoUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public int addNews(News news) {
        newsDAO.addNews(news);
        return news.getId();
    }

    public News getById(int newsId) {
        return newsDAO.getById(newsId);
    }

    /**
     * 更新资讯的评论数量
     * @param id 被评论的资讯id
     * @param count 评论数量
     * @return
     */
    public int updateCommentCount(int id, int count) {
        return newsDAO.updateCommentCount(id, count);
    }

    /**
     * 更新资讯的点赞数量
     * @param id 被点赞的资讯标识符id
     * @param likeCount 点赞数量
     * @return
     */
    public int updateLikeCount(int id, int likeCount) {
        return newsDAO.updateLikeCount(id, likeCount);
    }


    /**
     * 保存图片到本地
     * @param file
     * @return
     * @throws IOException
     */
    public String saveImage(MultipartFile file) throws IOException {
        String fileName = ToutiaoUtil.parseFileName(file);
        //String fileName = file.getOriginalFilename();
        Files.copy(file.getInputStream(), new File(ToutiaoUtil.IMG_DIR + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        //return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + fileName;
        return "/image?name=" + fileName;
    }
}
