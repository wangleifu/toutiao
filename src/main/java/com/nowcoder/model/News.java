package com.nowcoder.model;

import java.util.Date;

/**
 * 咨询类
 *
 * Created by rainday on 16/6/30.
 */
public class News {
    /**
     * 唯一标识符
     */
    private int id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容连接
     */
    private String link;
    /**
     * 图片连接
     */
    private String image;
    /**
     * 点赞数
     */
    private int likeCount;
    /**
     * 评论数
     */
    private int commentCount;
    /**
     * 创建时间
     */
    private Date createdDate;
    /**
     * 创建咨询的用户的id
     */
    private int userId;

    public int getId() {
    return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
