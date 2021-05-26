package com.example.commondemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 优惠券使用规则表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MallProductCouponRuleDO {
    /**
     * 主键Id
     */

    private String id;

    /**
     * 优惠券id
     */
    private String couponId;

    /**
     * 创建时间
     */
    private LocalDateTime createDt;

    /**
     * 创建人ID
     */
    private String creatorId;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 最后更新时间
     */
    private LocalDateTime lastUpdateDt;

    /**
     * 最后更新人ID
     */
    private String lastUpdatorId;

    /**
     * 最后更新人
     */
    private String lastUpdator;
}