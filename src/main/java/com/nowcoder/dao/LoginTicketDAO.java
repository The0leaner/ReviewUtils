package com.nowcoder.dao;

import com.nowcoder.model.LoginTicket;
import org.apache.ibatis.annotations.*;


/**
 * Created by lenovo on 2017/4/18.
 */
public interface LoginTicketDAO {
    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELDS =" user_id, expired, status, ticket ";
    String SELECT_FIELDS =" id " + INSERT_FIELDS;

    @Insert({" insert into ", TABLE_NAME ," (", INSERT_FIELDS ," ) values ({#uesrId},{#expired},{#status},{#ticket})"})
    int addTicket(LoginTicket ticket);

    @Select({" select ", SELECT_FIELDS ," from ", TABLE_NAME, " where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);
    //看用户在不在
    @Update({" update " ,TABLE_NAME , " set status=#{status} where ticket=#{ticket} " })
    void updateStatus(@Param("ticket") String ticket , @Param("status") int status);
    //status默认为0，登出时调整为1
}
