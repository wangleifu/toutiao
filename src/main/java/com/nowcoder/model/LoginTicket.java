package com.nowcoder.model;

import java.util.Date;

/**
 * 登录的token，在设定时间内免密登录
 *
 * @Author wangleifu
 * @data 2018/12/18
 */
public class LoginTicket {
    /**
     * 唯一标识符
     */
    private int id;
    /**
     * 对应的用户的id
     */
    private int userId;
    /**
     * 到期时间
     */
    private Date expired;
    /**
     * 判断当前ticket是否有效： 0有效，1无效
     */
    private int status;
    /**
     * ticket标识
     */
    private String ticket;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
