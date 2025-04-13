package com.yun.lottery.controller;

import com.yun.lottery.common.errorcode.ControllerErrorCodeConstants;
import com.yun.lottery.common.exception.ControllerException;
import com.yun.lottery.common.pojo.CommonResult;
import com.yun.lottery.common.utils.JacksonUtil;
import com.yun.lottery.controller.param.UserRegisterParam;
import com.yun.lottery.controller.result.UserRegisterResult;
import com.yun.lottery.service.UserService;
import com.yun.lottery.service.dto.UserRegisterDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yun
 * @date 2025/4/13 13:05
 * @desciption: 用户
 */
@Slf4j
@RestController
public class UserController {


    @Autowired
    private UserService userService;

    /**
     * 注册
     */
    @PostMapping("/register")
    public CommonResult<UserRegisterResult> userRegister(@Validated @RequestBody UserRegisterParam param) {
        // 日志打印
        log.info("userRegister UserRegisterParam:{}", JacksonUtil.writeValueAsString(param));
        // 调用service层
        UserRegisterDTO userRegisterDTO = userService.register(param);

        return CommonResult.success(convertToUserRegisterResult(userRegisterDTO));
    }

    private UserRegisterResult convertToUserRegisterResult(UserRegisterDTO userRegisterDTO) {
        UserRegisterResult result = new UserRegisterResult();
        if (userRegisterDTO == null) {
            throw new ControllerException(ControllerErrorCodeConstants.REGISTER_ERROR);
        }

        result.setUserId(userRegisterDTO.getUserId());
        return result;
    }

}
