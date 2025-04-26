package com.yun.lottery.service;

import com.yun.lottery.controller.param.CreateActivityParam;
import com.yun.lottery.controller.param.PageParam;
import com.yun.lottery.service.dto.ActivityDTO;
import com.yun.lottery.service.dto.ActivityDetailDTO;
import com.yun.lottery.service.dto.CreateActivityDTO;
import com.yun.lottery.service.dto.PageListDTO;

/**
 * @author yun
 * @date 2025/4/26 19:26
 * @desciption:
 */
public interface ActivityService {

    /**
     * 创建活动
     *
     * @param param
     * @return
     */
    CreateActivityDTO createActivity(CreateActivityParam param);

    /**
     * 翻页查询活动(摘要)列表
     *
     * @param param
     * @return
     */
    PageListDTO<ActivityDTO> findActivityList(PageParam param);

    /**
     * 获取活动详细属性
     *
     * @param activityId
     * @return
     */
    ActivityDetailDTO getActivityDetail(Long activityId);

    /**
     * 缓存活动详细信息（读取表数据 在缓存）
     *
     * @param activityId
     */
    void cacheActivity(Long activityId);
}
