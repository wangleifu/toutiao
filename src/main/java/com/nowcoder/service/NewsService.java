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

    public String saveImage(MultipartFile file) throws IOException {
        // 找的后缀名前的“.”
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            return null;
        }
        // 找到后缀名
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        // 判断后缀名是否符合要求
        if (!ToutiaoUtil.isFileAllowed(fileExt)) {
            return null;
        }

        //String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        String fileName = file.getOriginalFilename();
        Files.copy(file.getInputStream(), new File(ToutiaoUtil.IMG_DIR + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + fileName;
    }
}
