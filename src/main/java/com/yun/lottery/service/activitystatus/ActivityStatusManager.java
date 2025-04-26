package com.yun.lottery.service.activitystatus;

import com.yun.lottery.service.dto.ConvertActivityStatusDTO;

/**
 * @author yun
 * @date 2025/4/28 15:06
 * @desciption:
 */
public interface ActivityStatusManager {

    /**
     * 处理活动相关状态转换
     *
     * @param convertActivityStatusDTO
     */
    void handlerEvent(ConvertActivityStatusDTO convertActivityStatusDTO);


    /**
     * 回滚处理活动相关状态
     *
     * @param convertActivityStatusDTO
     */
    void rollbackHandlerEvent(ConvertActivityStatusDTO convertActivityStatusDTO);

}