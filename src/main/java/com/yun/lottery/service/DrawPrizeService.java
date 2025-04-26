package com.yun.lottery.service;

import com.yun.lottery.controller.param.DrawPrizeParam;
import org.springframework.stereotype.Service;

/**
 * @author yun
 * @date 2025/4/28 9:25
 * @desciption:
 */
@Service
public interface DrawPrizeService {

    /**
     * 异步抽奖接口
     *
     * @param param
     */
    void drawPrize(DrawPrizeParam param);

    /**
     * 校验抽奖请求
     *
     * @param param
     */
    Boolean checkDrawPrizeParam(DrawPrizeParam param);

    /**
     * 保存中奖者名单
     *
     * @param param
     */
    List<WinningRecordDO> saveWinnerRecords(DrawPrizeParam param);

    /**
     * 删除活动/奖品下的中奖记录
     *
     * @param activityId
     * @param prizeId
     */
    void deleteRecords(Long activityId, Long prizeId);

    /**
     * 获取中奖记录
     *
     * @param param
     * @return
     */
    List<WinningRecordDTO> getRecords(ShowWinningRecordsParam param);
}