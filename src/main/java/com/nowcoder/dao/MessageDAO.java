package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface MessageDAO {
    // 注意空格
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type , status  ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addMessage(Message message);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id} " })
    Comment selectCommentByEntity(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where conversation_id=#{conversationId} order by created_date desc"})
    List<Message>  getConversationDetail(@Param("conversationId") String conversationId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    //select * count(id) as cnt from (SELECT * FROM message order by created_date desc) tt group by conversationId order by created_date desc limit

    @Select({"select ", INSERT_FIELDS, " count(id) as id from (select * from ", TABLE_NAME, " where from_id=#{userId} or to_id = #{userId}by created_date desc) ott group by conversationId order by created_date desc limit=#{offset} #{limit}"})
    List<Message>  getConversationList(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    @Select({"selelct count(id) from", TABLE_NAME , "where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,
                        @Param("entityType") int entityType);

    @Select({"select count(id) from " ,TABLE_NAME , " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Update({"update comment set status=#{status}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);
}
