package com.yun.lottery.service.mq;

import cn.hutool.core.date.DateUtil;

import com.yun.lottery.common.exception.ServiceException;
import com.yun.lottery.common.utils.JacksonUtil;
import com.yun.lottery.common.utils.MailUtil;
import com.yun.lottery.common.utils.SMSUtil;
import com.yun.lottery.controller.param.DrawPrizeParam;
import com.yun.lottery.dao.dataobject.ActivityPrizeDO;
import com.yun.lottery.dao.dataobject.WinningRecordDO;
import com.yun.lottery.dao.mapper.ActivityPrizeMapper;
import com.yun.lottery.dao.mapper.WinningRecordMapper;
import com.yun.lottery.service.DrawPrizeService;
import com.yun.lottery.service.activitystatus.ActivityStatusManager;
import com.yun.lottery.service.dto.ConvertActivityStatusDTO;
import com.yun.lottery.service.enums.ActivityPrizeStatusEnum;
import com.yun.lottery.service.enums.ActivityPrizeTiersEnum;
import com.yun.lottery.service.enums.ActivityStatusEnum;
import com.yun.lottery.service.enums.ActivityUserStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yun.lottery.common.config.DirectRabbitConfig.QUEUE_NAME;

/**
 * @author yun
 * @date 2025/4/28 9:59
 * @desciption:
 */
@Slf4j
@Component
@RabbitListener(queues = QUEUE_NAME)
public class MqReceiver {

    @Autowired
    private DrawPrizeService drawPrizeService;
    @Autowired
    private ActivityStatusManager activityStatusManager;
    @Qualifier("asyncServiceExecutor")
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private MailUtil mailUtil;
    @Autowired
    private SMSUtil smsUtil;
    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;
    @Autowired
    private WinningRecordMapper winningRecordMapper;

    @RabbitHandler
    public void process(Map<String, String> message) throws Exception {
        // 成功接收到队列中的消息
        log.info("MQ成功接收到消息，message:{}",
                JacksonUtil.writeValueAsString(message));
        String paramString = message.get("messageData");
        DrawPrizeParam param = JacksonUtil.readValue(paramString, DrawPrizeParam.class);
        // 处理抽奖的流程

        try {

            // 校验抽奖请求是否有效
            // 1、有可能前端发起两个一样的抽奖请求，对于param来说也是一样的两个请求
            // 2、param：最后一个奖项-》
            //      处理param1：活动完成、奖品完成
            //      处理param2: 回滚活动、奖品状态
            if (!drawPrizeService.checkDrawPrizeParam(param)) {
                return;
            }

            // 状态扭转处理（重要！！ 设计模式）
            statusConvert(param);

            // 保存中奖者名单
            List<WinningRecordDO> winningRecordDOList =
                    drawPrizeService.saveWinnerRecords(param);

            // 通知中奖者（邮箱、短信）
            // 抽奖之后的后续流程，异步（并发）处理
            syncExecute(winningRecordDOList);

        } catch (ServiceException e) {
            log.error("处理 MQ 消息异常！{}:{}", e.getCode(), e.getMessage(), e);
            // 需要保证事务一致性（回滚）
            rollback(param);
            // 抛出异常: 消息重试（解决异常：代码bug、网络问题、服务问题）
            throw e;

        } catch (Exception e) {
            log.error("处理 MQ 消息异常！", e);
            // 需要保证事务一致性（回滚）
            rollback(param);
            // 抛出异常
            throw e;
        }

    }

    /**
     * 处理抽奖异常的回滚行为：恢复处理请求之前的库表状态
     *
     * @param param
     */
    private void rollback(DrawPrizeParam param) {

        // 1、回滚状态：活动、奖品、人员
        // 状态是否需要回滚
        if (!statusNeedRollback(param)) {
            // 不需要：return
            return;
        }
        // 需要回滚: 回滚
        rollbackStatus(param);

        // 2、回滚中奖者名单
        // 是否需要回滚
        if (!winnerNeedRollback(param)) {
            // 不需要：return
            return;
        }
        // 需要: 回滚
        rollbackWinner(param);

    }

    /**
     * 回滚中奖记录：删除奖品下的中奖者
     *
     * @param param
     */
    private void rollbackWinner(DrawPrizeParam param) {
        drawPrizeService.deleteRecords(param.getActivityId(), param.getPrizeId());
    }

    private boolean winnerNeedRollback(DrawPrizeParam param) {
        // 判断活动中的奖品是否存在中奖者
        int count = winningRecordMapper.countByAPId(param.getActivityId(), param.getPrizeId());
        return count > 0;
    }

    /**
     * 恢复相关状态
     *
     * @param param
     */
    private void rollbackStatus(DrawPrizeParam param) {
        // 涉及状态的恢复，使用 ActivityStatusManager
        ConvertActivityStatusDTO convertActivityStatusDTO = new ConvertActivityStatusDTO();
        convertActivityStatusDTO.setActivityId(param.getActivityId());
        convertActivityStatusDTO.setTargetActivityStatus(ActivityStatusEnum.RUNNING);
        convertActivityStatusDTO.setPrizeId(param.getPrizeId());
        convertActivityStatusDTO.setTargetPrizeStatus(ActivityPrizeStatusEnum.INIT);
        convertActivityStatusDTO.setUserIds(
                param.getWinnerList().stream()
                        .map(DrawPrizeParam.Winner::getUserId)
                        .collect(Collectors.toList())
        );
        convertActivityStatusDTO.setTargetUserStatus(ActivityUserStatusEnum.INIT);
        activityStatusManager.rollbackHandlerEvent(convertActivityStatusDTO);

    }

    private boolean statusNeedRollback(DrawPrizeParam param) {
        // 判断活动+奖品+人员表相关状态是否已经扭转（正常思路）
        // 扭转状态时，保证了事务一致性，要么都扭转了，要么都没扭转（不包含活动）：
        // 因此，只用判断人员/奖品是否扭转过，就能判断出状态是否全部扭转
        // 不能判断活动是否已经扭转
        // 结论：判断奖品状态是否扭转，就能判断出全部状态是否扭转
        ActivityPrizeDO activityPrizeDO =
                activityPrizeMapper.selectByAPId(param.getActivityId(), param.getPrizeId());
        // 已经扭转了，需要回滚
        return activityPrizeDO.getStatus()
                .equalsIgnoreCase(ActivityPrizeStatusEnum.COMPLETED.name());

    }

    /**
     * 并发处理抽奖后续流程
     *
     * @param winningRecordDOList
     */
    private void syncExecute(List<WinningRecordDO> winningRecordDOList) {
        // 通过线程池 threadPoolTaskExecutor
        // 扩展：加入策略模式或者其他设计模式来完成后续的异步操作
        // 短信通知
        threadPoolTaskExecutor.execute(() -> sendMessage(winningRecordDOList));
        // 邮件通知
        threadPoolTaskExecutor.execute(() -> sendMail(winningRecordDOList));
    }

    /**
     * 发邮件
     *
     * @param winningRecordDOList
     */
    private void sendMail(List<WinningRecordDO> winningRecordDOList) {
        if (CollectionUtils.isEmpty(winningRecordDOList)) {
            log.info("中奖列表为空，不用发邮件！");
            return;
        }
        for (WinningRecordDO winningRecordDO : winningRecordDOList) {
            // Hi,胡一博。恭喜你在抽奖活动活动中获得二等奖:吹风机。获奖奖时间为18:18:44,请尽快领取您的奖励
            String context = "Hi，" + winningRecordDO.getWinnerName() + "。恭喜你在"
                    + winningRecordDO.getActivityName() + "活动中获得"
                    + ActivityPrizeTiersEnum.forName(winningRecordDO.getPrizeTier()).getMessage()
                    + "：" + winningRecordDO.getPrizeName() + "。获奖时间为"
                    + DateUtil.formatTime(winningRecordDO.getWinningTime()) + "，请尽快领 取您的奖励！";
            mailUtil.sendSampleMail(winningRecordDO.getWinnerEmail(),
                    "中奖通知", context);
        }
    }

    /**
     * 发短信
     *
     * @param winningRecordDOList
     */
    private void sendMessage(List<WinningRecordDO> winningRecordDOList) {
        if (CollectionUtils.isEmpty(winningRecordDOList)) {
            log.info("中奖列表为空，不用发短信！");
            return;
        }
        for (WinningRecordDO winningRecordDO : winningRecordDOList) {
            Map<String, String> map = new HashMap<>();
            map.put("name", winningRecordDO.getWinnerName());
            map.put("activityName", winningRecordDO.getActivityName());
            map.put("prizeTiers", ActivityPrizeTiersEnum.forName(winningRecordDO.getPrizeTier()).getMessage());
            map.put("prizeName", winningRecordDO.getPrizeName());
            map.put("winningTime", DateUtil.formatTime(winningRecordDO.getWinningTime()));
            smsUtil.sendMessage("SMS_465985911",
                    winningRecordDO.getWinnerPhoneNumber().getValue(),
                    JacksonUtil.writeValueAsString(map));
        }
    }

    /**
     * 状态扭转
     *
     * @param param
     */
    private void statusConvert(DrawPrizeParam param) {
        ConvertActivityStatusDTO convertActivityStatusDTO = new ConvertActivityStatusDTO();
        convertActivityStatusDTO.setActivityId(param.getActivityId());
        convertActivityStatusDTO.setTargetActivityStatus(ActivityStatusEnum.COMPLETED);
        convertActivityStatusDTO.setPrizeId(param.getPrizeId());
        convertActivityStatusDTO.setTargetPrizeStatus(ActivityPrizeStatusEnum.COMPLETED);
        convertActivityStatusDTO.setUserIds(
                param.getWinnerList().stream()
                        .map(DrawPrizeParam.Winner::getUserId)
                        .collect(Collectors.toList())
        );
        convertActivityStatusDTO.setTargetUserStatus(ActivityUserStatusEnum.COMPLETED);
        activityStatusManager.handlerEvent(convertActivityStatusDTO);
    }

//    private void statusConvert(DrawPrizeParam param) {
//
//        // 问题：
//        // 1、活动状态扭转有依赖性，导致代码维护性差
//        // 2、状态扭转条件可能会扩展，当前写法，扩展性差，维护性差
//        // 3、代码的灵活性、扩展性、维护性极差
//        // 解决方案：设计模式（责任链设计模式、策略模式）
//
//
//        // 活动: RUNNING-->COMPLETED ??  全部奖品抽完之后才改变状态
//        // 奖品：INIT-->COMPLETED
//        // 人员列表: INIT-->COMPLETED
//
//        // 1 扭转奖品状态
//        // 查询活动关联的奖品信息
//        // 条件判断是否符合扭转奖品状态：判断当前状态是否 不是COMPLETED，如果不是，要扭转
//        // 才去扭转
//
//        // 2 扭转人员状态
//        // 查询活动关联的人员信息
//        // 条件判断是否符合扭转人员状态：判断当前状态是否 不是COMPLETED，如果不是，要扭转
//        // 才去扭转
//
//        // 4、扭转xxx状态
//
//        // 3 扭转活动状态（必须再扭转奖品状态之后完成）
//        // 查询活动信息
//        // 条件判断是否符合扭转活动状态：才改变状态
//        //      判断当前状态是否 不是COMPLETED，如果不是，
//        //      且全部奖品抽完之后，
//        //      （且xxx状态扭转）
//        // 才去扭转
//
//
//        // 4 更新活动完整信息缓存
//
//    }

}