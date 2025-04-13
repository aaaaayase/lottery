package com.yun.lottery.common.errorcode;

/**
 * @author yun
 * @date 2025/4/12 15:47
 * @desciption: 全局错误吗
 */
public class GlobalErrorCodeConstants {

    public static ErrorCode SUCCESS = new ErrorCode(200, "成功！");

    public static ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode(500, "系统异常！");

    public static ErrorCode UNKNOWN = new ErrorCode(999, "未知错误");


}
