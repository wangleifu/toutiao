package com.nowcoder.model;

/**
 * Created by nowcoder on 2016/6/26.
 */
public class User {
    private int id;
    /**
     * 登录名
     */
    private String name;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 密码加密
     */
    private String salt;
    /**
     * 头像连接地址
     */
    private String headUrl;

    public User() {

    }
    public User(String name) {
        this.name = name;
        this.password = "";
        this.salt = "";
        this.headUrl = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
