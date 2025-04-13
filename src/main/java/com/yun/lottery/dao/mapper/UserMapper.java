package com.yun.lottery.dao.mapper;

import com.yun.lottery.dao.dataobject.Encrypt;
import com.yun.lottery.dao.dataobject.UserDO;
import org.apache.ibatis.annotations.*;

/**
 * @author yun
 * @date 2025/4/13 14:59
 * @desciption: 用户
 */
@Mapper
public interface UserMapper {
    /**
     * 查询邮箱绑定的人数
     *
     * @param email
     * @return
     */
    @Select("select count(*) from user where email=#{email}")
    int countByMail(@Param("email") String email);

    @Select("select count(*) from user where phoneNumber=#{phoneNumber}")
    int countByPhone(@Param("phoneNumber") Encrypt phoneNumber);

    @Insert("insert into user(user_name,email,phone_number,password,identity) values (#{userName},#{email},#{phoneNumber},#{password},#{identity})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void insert(UserDO userDO);
}
