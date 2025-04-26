package com.yun.lottery.common.errorcode;

/**
 * @author yun
 * @date 2025/4/12 15:47
 * @desciption: Controller层错误吗
 */
public interface ControllerErrorCodeConstants {

    // --------人员模块错误吗
    ErrorCode REGISTER_ERROR = new ErrorCode(100, "注册失败");
    ErrorCode LOGIN_ERROR = new ErrorCode(101, "登录失败");


    // --------活动模块错误吗
    ErrorCode CREATE_ACTIVITY_ERROR = new ErrorCode(201, "创建活动失败");
    ErrorCode FIND_ACTIVITY_LIST_ERROR = new ErrorCode(202, "查询活动列表失败");
    ErrorCode GET_ACTIVITY_DETAIL_ERROR = new ErrorCode(203, "查询活动详情失败");
    // --------奖品模块错误吗
    ErrorCode FIND_PRIZE_LIST_ERROR = new ErrorCode(300, "查询奖品列表失败");
    // --------抽奖错误吗


}
