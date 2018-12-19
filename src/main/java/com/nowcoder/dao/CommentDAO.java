package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    /**
     *  添加Comment
     * @param comment 评论实体
     * @return
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    /**
     * 通过entity_id 和 entity_type 获取评论
     * @param entityId    id
     * @param entityType  类型
     * @return
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where entity_type=#{entityType} and entity_id=#{entityId} order by id desc "})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    /**
     * 获取评论数
     * @param entityId   id
     * @param entityType 类型
     * @return
     */
    @Select({"select count(id) from ", TABLE_NAME, " where entity_type=#{entityType} and entity_id=#{entityId}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    /**
     * 更新评论状态
     * @param entityId   id
     * @param entityType 类型
     * @param status     状态
     */
    @Update({"update ", TABLE_NAME,"set status=#{status} where entity_type=#{entityType} and entity_id=#{entityId}"})
    void updateComment(@Param("entityId")int entityId, @Param("entityType") int entityType, @Param("status") int status);

}
