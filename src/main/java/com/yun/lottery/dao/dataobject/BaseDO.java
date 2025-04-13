package com.yun.lottery.dao.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yun
 * @date 2025/4/13 16:13
 * @desciption:
 */
@Data
public class BaseDO implements Serializable {

    private Long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;
}
