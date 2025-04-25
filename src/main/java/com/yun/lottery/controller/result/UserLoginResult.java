package com.yun.lottery.controller.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: yibo
 */
@Data
public class UserLoginResult implements Serializable {

    /**
     * JWT 令牌
     */
    private String token;

    /**
     * 登录人员身份
     */
    private String identity;

}
