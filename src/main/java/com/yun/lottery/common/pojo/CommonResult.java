package com.yun.lottery.common.pojo;

import com.yun.lottery.common.errorcode.ErrorCode;
import com.yun.lottery.common.errorcode.GlobalErrorCodeConstants;
import lombok.Data;
import org.springframework.util.Assert;

import javax.management.loading.PrivateClassLoader;
import java.io.Serializable;

/**
 * @author yun
 * @date 2025/4/12 16:19
 * @desciption: 返回结果
 */
@Data
public class CommonResult<T> implements Serializable {

    private int code;

    private T data;

    private String msg;


    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.code = GlobalErrorCodeConstants.SUCCESS.getCode();
        result.msg = GlobalErrorCodeConstants.SUCCESS.getMsg();
        result.data = data;
        return result;
    }

    public static <T> CommonResult<T> error(Integer code, String msg) {
        Assert.isTrue(!GlobalErrorCodeConstants.SUCCESS.equals(code), "code 不是错误的异常");
        CommonResult<T> result = new CommonResult<>();
        result.code = code;
        result.msg = msg;

        return result;
    }

    public static <T> CommonResult<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }
}
