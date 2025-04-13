package com.yun.lottery.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.yun.lottery.common.errorcode.ServiceErrorCodeConstants;
import com.yun.lottery.common.exception.ServiceException;
import com.yun.lottery.common.utils.RegexUtil;
import com.yun.lottery.controller.param.UserRegisterParam;
import com.yun.lottery.dao.dataobject.Encrypt;
import com.yun.lottery.dao.dataobject.UserDO;
import com.yun.lottery.dao.mapper.UserMapper;
import com.yun.lottery.service.UserService;
import com.yun.lottery.service.dto.UserRegisterDTO;
import com.yun.lottery.service.enums.UserIdentityEnum;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

/**
 * @author yun
 * @date 2025/4/13 13:16
 * @desciption: 用户实现
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

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
}
