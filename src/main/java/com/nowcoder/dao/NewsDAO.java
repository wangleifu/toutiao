package com.nowcoder.dao;

import com.nowcoder.model.News;
import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title, link, image, like_count, comment_count, created_date, user_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * 添加资讯
     * @param news 要添加的咨询实体
     * @return
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    /**
     * 获取资讯实体
     * @param newsId 要获取的咨询的Id
     * @return
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where id=#{id}"})
    News getById(int newsId);

    /**
     * 更新资讯的评论数量
     * @param id  被评论的资讯id
     * @param commentCount 最新的评论数量
     * @return
     */
    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    /**
     * 默认获取首页资讯列表，若指定userId，则获取指定userId的资讯列表
     * @param userId 用户的Id
     * @param offset 选取的偏移量
     * @param limit  选取的数量
     * @return
     */
    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
                                       @Param("limit") int limit);
}
