package com.nowcoder.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author: wang
 * @Desciption: 向数据库测试数据表发送请求，维持数据库连接
 * @Date: Created in 11:23 2019/1/6
 * @Modified By:
 **/
@Mapper
public interface TestConnectionDao {

    String TABLE_NAME = "tb_test";
    String SELECT_FIELDS = "id";

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME})
    int query();

}
