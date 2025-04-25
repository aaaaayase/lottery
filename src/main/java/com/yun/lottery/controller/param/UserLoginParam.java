package com.yun.lottery.controller.param;

import com.yun.lottery.service.enums.UserIdentityEnum;
import com.yun.lottery.service.enums.UserIdentityEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: yun
 */
@Data
public class UserLoginParam implements Serializable {

    /**
     * 强制某身份登录。不填不限制身份
     * @see UserIdentityEnum#name()
     */
    private String mandatoryIdentity;

}
