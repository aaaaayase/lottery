package com.yun.lottery.controller;

import com.yun.lottery.common.errorcode.ControllerErrorCodeConstants;
import com.yun.lottery.common.exception.ControllerException;
import com.yun.lottery.common.pojo.CommonResult;
import com.yun.lottery.common.utils.JacksonUtil;
import com.yun.lottery.controller.param.ShortMessageLoginParam;
import com.yun.lottery.controller.param.UserPasswordLoginParam;
import com.yun.lottery.controller.param.UserRegisterParam;
import com.yun.lottery.controller.result.UserLoginResult;
import com.yun.lottery.controller.result.UserRegisterResult;
import com.yun.lottery.service.UserService;
import com.yun.lottery.service.VerificationCodeService;
import com.yun.lottery.service.dto.UserLoginDTO;
import com.yun.lottery.service.dto.UserRegisterDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Autowired
    private VerificationCodeService verificationCodeService;

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

    /**
     * 发送验证码
     *
     * @param phoneNumber
     * @return
     */
    @RequestMapping("/verification-code/send")
    public CommonResult<Boolean> sendVerificationCode(String phoneNumber) {
        log.info("sendVerificationCode phoneNumber:{}", phoneNumber);
        verificationCodeService.sendVerificationCode(phoneNumber);
        return CommonResult.success(Boolean.TRUE);
    }

    /**
     * 密码登录
     *
     * @param param
     * @return
     */
    @RequestMapping("/password/login")
    public CommonResult<UserLoginResult> userPasswordLogin(
            @Validated @RequestBody UserPasswordLoginParam param) {
        log.info("userPasswordLogin UserPasswordLoginParam:{}",
                JacksonUtil.writeValueAsString(param));
        UserLoginDTO userLoginDTO = userService.login(param);
        return CommonResult.success(convertToUserLoginResult(userLoginDTO));

    }

    private UserLoginResult convertToUserLoginResult(UserLoginDTO userLoginDTO) {
        if (null == userLoginDTO) {
            throw new ControllerException(ControllerErrorCodeConstants.LOGIN_ERROR);
        }

        UserLoginResult result = new UserLoginResult();
        result.setToken(userLoginDTO.getToken());
        result.setIdentity(userLoginDTO.getIdentity().name());
        return result;
    }

    /**
     * 短信验证码登录
     *
     * @param param
     * @return
     */
    @RequestMapping("/message/login")
    public CommonResult<UserLoginResult> shortMessageLogin(
            @Validated @RequestBody ShortMessageLoginParam param) {
        log.info("shortMessageLogin ShortMessageLoginParam:{}",
                JacksonUtil.writeValueAsString(param));
        UserLoginDTO userLoginDTO = userService.login(param);
        return CommonResult.success(convertToUserLoginResult(userLoginDTO));

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
