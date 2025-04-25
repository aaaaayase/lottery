package com.yun.lottery.service;

/**
 * @author yun
 * @date 2025/4/14 10:23
 * @desciption:
 */
public interface VerificationCodeService {
    /**
     * 发送验证码
     *
     * @param phoneNumber
     */
    void sendVerificationCode(String phoneNumber);

    /**
     * 从缓存中获取验证码
     *
     * @param phoneNumber
     * @return
     */
    String getVerificationCode(String phoneNumber);
}
