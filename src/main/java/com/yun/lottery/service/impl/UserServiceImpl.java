package com.yun.lottery.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.yun.lottery.common.errorcode.ServiceErrorCodeConstants;
import com.yun.lottery.common.exception.ServiceException;
import com.yun.lottery.common.utils.JWTUtil;
import com.yun.lottery.common.utils.RegexUtil;
import com.yun.lottery.controller.param.ShortMessageLoginParam;
import com.yun.lottery.controller.param.UserLoginParam;
import com.yun.lottery.controller.param.UserPasswordLoginParam;
import com.yun.lottery.controller.param.UserRegisterParam;
import com.yun.lottery.dao.dataobject.Encrypt;
import com.yun.lottery.dao.dataobject.UserDO;
import com.yun.lottery.dao.mapper.UserMapper;
import com.yun.lottery.service.UserService;
import com.yun.lottery.service.VerificationCodeService;
import com.yun.lottery.service.dto.UserDTO;
import com.yun.lottery.service.dto.UserLoginDTO;
import com.yun.lottery.service.dto.UserRegisterDTO;
import com.yun.lottery.service.enums.UserIdentityEnum;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yun
 * @date 2025/4/13 13:16
 * @desciption: 用户实现
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Override
    public UserRegisterDTO register(UserRegisterParam param) {
        // 校验注册信息
        checkRegisterInfo(param);
        // 加密私密数据
        UserDO userDO = new UserDO();
        userDO.setUserName(param.getName());
        userDO.setIdentity(param.getIdentity());
        userDO.setEmail(param.getMail());
        userDO.setPhoneNumber(new Encrypt(param.getPhoneNumber()));
        if (StringUtils.hasText(param.getPassword())) {
            userDO.setPassword(DigestUtil.sha256Hex(param.getPassword()));
        }
        // 保存数据
        userMapper.insert(userDO);

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUserId(userDO.getId());
        return userRegisterDTO;
    }

    private void checkRegisterInfo(UserRegisterParam param) {
        if (param == null) {
            throw new ServiceException(ServiceErrorCodeConstants.REGISTER_INFO_IS_EMPTY);
        }
        // 校验邮箱格式
        if (!RegexUtil.checkMail(param.getMail())) {
            throw new ServiceException(ServiceErrorCodeConstants.MAIL_ERROR);
        }
        // 校验手机号格式
        if (!RegexUtil.checkMobile(param.getPhoneNumber())) {
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_ERROR);
        }
        // 校验身份信息
        if (UserIdentityEnum.forName(param.getIdentity()) == null) {
            throw new ServiceException(ServiceErrorCodeConstants.IDENTITY_ERROR);
        }
        // 校验管理员密码必填
        if (param.getIdentity().equalsIgnoreCase(UserIdentityEnum.ADMIN.name()) && !StringUtils.hasText(param.getPassword())) {
            throw new ServiceException(ServiceErrorCodeConstants.PASSWORD_IS_EMPTY);
        }
        // 密码校验 至少6位
        if (StringUtils.hasText(param.getPassword()) && !RegexUtil.checkPassword(param.getPassword())) {
            throw new ServiceException(ServiceErrorCodeConstants.PASSWORD_ERROR);
        }
        // 校验邮箱是否被使用
        if (checkMailUsed(param.getMail())) {
            throw new ServiceException(ServiceErrorCodeConstants.MAIL_USED);
        }
        // 校验手机号是否被使用
        if (checkPhoneNumberUser(param.getPhoneNumber())) {
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_USED);
        }

    }

    private boolean checkPhoneNumberUser(String phoneNumber) {
        int count = userMapper.countByPhone(new Encrypt(phoneNumber));
        return count > 0;
    }

    private boolean checkMailUsed(String mail) {
        int count = userMapper.countByMail(mail);
        return count > 0;

    }

    @Override
    public UserLoginDTO login(UserLoginParam param) {
        UserLoginDTO userLoginDTO;

        // 类型检查与类型转换，java 14及以上版本
        if (param instanceof UserPasswordLoginParam loginParam) {
            // 密码登录流程
            userLoginDTO = loginByUserPassword(loginParam);
        } else if (param instanceof ShortMessageLoginParam loginParam) {
            // 短信验证码登录流程
            userLoginDTO = loginByShortMessage(loginParam);
        } else {
            throw new ServiceException(ServiceErrorCodeConstants.LOGIN_INFO_NOT_EXIST);
        }

        return userLoginDTO;
    }

    /**
     * 验证码登录
     *
     * @param loginParam
     * @return
     */
    private UserLoginDTO loginByShortMessage(ShortMessageLoginParam loginParam) {
        if (!RegexUtil.checkMobile(loginParam.getLoginMobile())) {
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_ERROR);
        }

        // 获取用户数据
        UserDO userDO = userMapper.selectByPhoneNumber(
                new Encrypt(loginParam.getLoginMobile()));
        if (null == userDO) {
            throw new ServiceException(ServiceErrorCodeConstants.USER_INFO_IS_EMPTY);
        } else if (StringUtils.hasText(loginParam.getMandatoryIdentity())
                && !loginParam.getMandatoryIdentity()
                .equalsIgnoreCase(userDO.getIdentity())) {
            throw new ServiceException(ServiceErrorCodeConstants.IDENTITY_ERROR);
        }

        // 校验验证码
        String code = verificationCodeService.getVerificationCode(
                loginParam.getLoginMobile());
        if (!loginParam.getVerificationCode().equals(code)) {
            throw new ServiceException(ServiceErrorCodeConstants.VERIFICATION_CODE_ERROR);
        }

        // 塞入返回值（JWT）
        Map<String, Object> claim = new HashMap<>();
        claim.put("id", userDO.getId());
        claim.put("identity", userDO.getIdentity());
        String token = JWTUtil.genJwt(claim);

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setToken(token);
        userLoginDTO.setIdentity(UserIdentityEnum.forName(userDO.getIdentity()));
        return userLoginDTO;


    }

    /**
     * 密码登录
     *
     * @param loginParam
     * @return
     */
    private UserLoginDTO loginByUserPassword(UserPasswordLoginParam loginParam) {

        UserDO userDO = null;
        // 判断手机登录还是邮箱登录
        if (RegexUtil.checkMail(loginParam.getLoginName())) {
            // 邮箱登录
            // 根据邮箱查询用户表
            userDO = userMapper.selectByMail(loginParam.getLoginName());
        } else if (RegexUtil.checkMobile(loginParam.getLoginName())) {
            // 手机号登录
            // 根据手机号查询用户表
            userDO = userMapper.selectByPhoneNumber(new Encrypt(loginParam.getLoginName()));
        } else {
            throw new ServiceException(ServiceErrorCodeConstants.LOGIN_NOT_EXIST);
        }

        // 校验登录信息
        if (null == userDO) {
            throw new ServiceException(ServiceErrorCodeConstants.USER_INFO_IS_EMPTY);
        } else if (StringUtils.hasText(loginParam.getMandatoryIdentity())
                && !loginParam.getMandatoryIdentity()
                .equalsIgnoreCase(userDO.getIdentity())) {
            // 强制身份登录，身份校验不通过
            throw new ServiceException(ServiceErrorCodeConstants.IDENTITY_ERROR);
        } else if (!DigestUtil.sha256Hex(loginParam.getPassword())
                .equals(userDO.getPassword())) {
            // 校验密码不同
            throw new ServiceException(ServiceErrorCodeConstants.PASSWORD_ERROR);
        }

        // 塞入返回值（JWT）
        Map<String, Object> claim = new HashMap<>();
        claim.put("id", userDO.getId());
        claim.put("identity", userDO.getIdentity());
        String token = JWTUtil.genJwt(claim);

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setToken(token);
        userLoginDTO.setIdentity(UserIdentityEnum.forName(userDO.getIdentity()));
        return userLoginDTO;

    }

    @Override
    public List<UserDTO> findUserInfo(UserIdentityEnum identity) {
        String identityString =  null == identity ? null : identity.name();
        // 查表
        List<UserDO> userDOList = userMapper.selectUserListByIdentity(identityString);
        List<UserDTO> userDTOList = userDOList.stream()
                .map(userDO -> {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setUserId(userDO.getId());
                    userDTO.setUserName(userDO.getUserName());
                    userDTO.setEmail(userDO.getEmail());
                    userDTO.setPhoneNumber(userDO.getPhoneNumber().getValue());
                    userDTO.setIdentity(UserIdentityEnum.forName(userDO.getIdentity()));
                    return userDTO;
                }).collect(Collectors.toList());
        return userDTOList;
    }


}
