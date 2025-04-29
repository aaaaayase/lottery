package com.yun.lottery.service.dto;

import com.yun.lottery.service.enums.ActivityPrizeTiersEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author yun
 * @date 2025/4/29 10:06
 * @desciption:
 */
@Data
public class WinningRecordDTO {
    /**
     * 中奖者id
     */
    private Long winnerId;

    /**
     * 中奖者姓名
     */
    private String winnerName;

    /**
     * 奖品名
     */
    private String prizeName;

    /**
     * 等级
     */
    private ActivityPrizeTiersEnum prizeTier;

    /**
     * 中奖时间
     */
    private Date winningTime;

}