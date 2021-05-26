package com.example.commondemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券信息表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponDO {
    /**
     * 主键ID-UUID
     */

    private String id;

    /**
     * 店铺id
     */
    private String storeId;

    /**
     * 标题
     */
    private String title;

    /**
     * 使用类型：0通用 1部分可用
     */
    private Integer useType;

    /**
     * 是否已上架[0=未上架][1=已上架]
     */
    private Boolean hasShelves;

    /**
     * 优惠方式[1=满减][2=兑换券]
     */
    private Integer couponWay;

    /**
     * 状态：0未激活1已使用2已过期
     */
    private Integer couponStatus;

    /**
     * 领取数量
     */
    private Integer getNumber;

    /**
     * 发放数量
     */
    private Integer couponCount;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 使用说明
     */
    private String useDescribe;

    /**
     * 有效开始时间
     */
    private LocalDateTime startDt;

    /**
     * 有效结束时间
     */
    private LocalDateTime endDt;

    /**
     * 使用范围
     */
    private Integer scopeOfUse;

    /**
     * 优惠券类型
     */
    private Integer couponType;

    /**
     * 备注
     */
    private String remark;

    private Integer memberPackage;


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

    /**
     * 是否删除[0=未删除][1=已删除]
     */
    private Integer deleted;
}