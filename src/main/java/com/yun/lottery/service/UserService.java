package com.yun.lottery.service;

import com.yun.lottery.controller.param.UserLoginParam;
import com.yun.lottery.controller.param.UserPasswordLoginParam;
import com.yun.lottery.controller.param.UserRegisterParam;
import com.yun.lottery.controller.result.UserRegisterResult;
import com.yun.lottery.service.dto.UserDTO;
import com.yun.lottery.service.dto.UserLoginDTO;
import com.yun.lottery.service.dto.UserRegisterDTO;
import com.yun.lottery.service.enums.UserIdentityEnum;

import java.util.List;

/**
 * @author yun
 * @date 2025/4/13 13:14
 * @desciption: 用户
 */
public interface UserService {

    UserRegisterDTO register(UserRegisterParam param);

    UserLoginDTO login(UserLoginParam param);

    List<UserDTO> findUserInfo(UserIdentityEnum userIdentityEnum);
}
