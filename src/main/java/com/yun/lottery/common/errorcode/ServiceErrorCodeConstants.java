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
    ErrorCode LOGIN_INFO_NOT_EXIST = new ErrorCode(108, "登录信息不存在");
    ErrorCode LOGIN_NOT_EXIST = new ErrorCode(109, "登录方式不存在");
    ErrorCode USER_INFO_IS_EMPTY = new ErrorCode(110, "用户信息为空");
    ErrorCode VERIFICATION_CODE_ERROR = new ErrorCode(111, "验证码校验失败");


    // --------活动模块错误吗
    ErrorCode CREATE_ACTIVITY_INFO_IS_EMPTY = new ErrorCode(201, "创建的活动信息为空");
    ErrorCode ACTIVITY_USER_ERROR = new ErrorCode(202, "活动关联人员异常");
    ErrorCode ACTIVITY_PRIZE_ERROR = new ErrorCode(203, "活动关联奖品异常");
    ErrorCode USER_PRIZE_AMOUNT_ERROR = new ErrorCode(204, "活动关联奖品及人员数量异常");
    ErrorCode ACTIVITY_STATUS_CONVERT_ERROR = new ErrorCode(205, "活动相关状态转换失败");
    ErrorCode CACHE_ACTIVITY_ID_IS_EMPTY = new ErrorCode(206, "缓存活动的活动id为空");
    ErrorCode CACHE_ACTIVITY_ID_ERROR = new ErrorCode(207, "缓存活动的活动id有误");

    // --------奖品模块错误吗

    // --------抽奖错误吗
    ErrorCode ACTIVITY_OR_PRIZE_IS_EMPTY = new ErrorCode(400, "抽奖活动或关联奖品不存在");
    ErrorCode ACTIVITY_COMPLETED = new ErrorCode(401, "抽奖活动已完成，无法抽奖");
    ErrorCode ACTIVITY_PRIZE_COMPLETED = new ErrorCode(402, "当前奖品已被抽取，无法抽奖");
    ErrorCode WINNER_PRIZE_AMOUNT_ERROR = new ErrorCode(403, "中奖人数与奖品数量不一致，无法抽奖");


    // --------图片错误码
    ErrorCode PIC_UPLOAD_ERROR = new ErrorCode(500, "图片上传失败");

}
