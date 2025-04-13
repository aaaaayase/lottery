package com.yun.lottery.common.errorcode;

/**
 * @author yun
 * @date 2025/4/12 15:47
 * @desciption: Service层错误吗
 */
public interface ServiceErrorCodeConstants {

    // --------人员模块错误吗
    ErrorCode REGISTER_INFO_IS_EMPTY = new ErrorCode(100, "注册信息为空");
    ErrorCode MAIL_ERROR = new ErrorCode(101, "邮箱格式错误");
    ErrorCode PHONE_NUMBER_ERROR = new ErrorCode(102, "手机号错误");
    ErrorCode IDENTITY_ERROR = new ErrorCode(103, "身份错误");
    ErrorCode PASSWORD_IS_EMPTY = new ErrorCode(104, "密码为空");
    ErrorCode PASSWORD_ERROR = new ErrorCode(105, "密码错误");
    ErrorCode MAIL_USED = new ErrorCode(106, "邮箱已被使用");
    ErrorCode PHONE_NUMBER_USED = new ErrorCode(107, "手机号已被使用");

    // --------活动模块错误吗

    // --------奖品模块错误吗

    // --------抽奖错误吗


}
