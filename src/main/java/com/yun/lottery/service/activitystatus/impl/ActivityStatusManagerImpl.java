package com.yun.lottery.service.activitystatus.impl;

import com.yun.lottery.common.errorcode.ServiceErrorCodeConstants;
import com.yun.lottery.common.exception.ServiceException;
import com.yun.lottery.service.ActivityService;
import com.yun.lottery.service.activitystatus.ActivityStatusManager;
import com.yun.lottery.service.activitystatus.operator.AbstractActivityOperator;
import com.yun.lottery.service.dto.ConvertActivityStatusDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author yun
 * @date 2025/4/28 15:19
 * @desciption:
 */
@Component
@Slf4j
public class ActivityStatusManagerImpl implements ActivityStatusManager {

    @Autowired
    private final Map<String, AbstractActivityOperator> operatorMap = new HashMap<>();
    @Autowired
    private ActivityService activityService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlerEvent(ConvertActivityStatusDTO convertActivityStatusDTO) {
        // 1、活动状态扭转有依赖性，导致代码维护性差
        // 2、状态扭转条件可能会扩展，当前写法，扩展性差，维护性差

        if (CollectionUtils.isEmpty(operatorMap)) {
            log.warn("operatorMap 为空！");
            return;
        }
        Map<String, AbstractActivityOperator> currMap = new HashMap<>(operatorMap);
        Boolean update = false;
        // 先处理：人员、奖品
        update = processConvertStatus(convertActivityStatusDTO, currMap, 1);
        // 后处理：活动
        update = processConvertStatus(convertActivityStatusDTO, currMap, 2) || update;
        // 更新缓存
        if (update) {
            activityService.cacheActivity(convertActivityStatusDTO.getActivityId());
        }

    }

    @Override
    public void rollbackHandlerEvent(ConvertActivityStatusDTO convertActivityStatusDTO) {
        // operatorMap：活动、奖品、人员
        // 活动是否需要回滚？？ 绝对需要，
        // 原因：奖品都恢复成INIT，那么这个活动下的奖品绝对没抽完
        for (AbstractActivityOperator operator : operatorMap.values()) {
            operator.convert(convertActivityStatusDTO);
        }

        // 缓存更新
        activityService.cacheActivity(convertActivityStatusDTO.getActivityId());
    }

    /**
     * 扭转状态
     *
     * @param convertActivityStatusDTO
     * @param currMap
     * @param sequence
     * @return
     */
    private Boolean processConvertStatus(ConvertActivityStatusDTO convertActivityStatusDTO,
                                         Map<String, AbstractActivityOperator> currMap,
                                         int sequence) {
        Boolean update = false;
        // 遍历currMap
        Iterator<Map.Entry<String, AbstractActivityOperator>> iterator = currMap.entrySet().iterator();
        while (iterator.hasNext()) {
            AbstractActivityOperator operator = iterator.next().getValue();
            // Operator 是否需要转换
            if (operator.sequence() != sequence
                    || !operator.needConvert(convertActivityStatusDTO)) {
                continue;
            }

            // 需要转换：转换
            if (!operator.convert(convertActivityStatusDTO)) {
                log.error("{}状态转换失败！", operator.getClass().getName());
                throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_STATUS_CONVERT_ERROR);
            }

            // currMap 删除当前 Operator
            iterator.remove();
            update = true;
        }

        // 返回
        return update;
    }
}
