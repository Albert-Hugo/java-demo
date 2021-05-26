package com.example.commondemo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: chenhaiyang
 * @create: 2020-09-18 14:55
 **/
public class DeliveryReturnVisitDO {
    private String id;
    /**
     * 服务单ID
     */
    private String serviceNoId;

    private String orderNo;
    /**
     * 被评论者id
     */
    private String commentedId;
    /**
     * 评价得分
     */
    private String commentScore;
    private String commentMore;

    /**
     * 客户评价时间
     */
    private LocalDateTime commentDate;
    /**
     * 回访用户ID
     */
    private String userId;
    /**
     * \
     * 回访用户
     */
    private String userName;
    /**
     *
     */
    private String remark;
    /**
     * 类型id:0-回访评价
     */
    private String typeId;
    /**
     * 记录人
     */
    private String createBy;
    /**
     * 记录人
     */
    private String createName;
    /**
     * 创建时间
     */
    private String createTime;
    private String history;
    private String postType;
    private String commentFlag;
    private String picId;

    private List<DeliveryReturnVisitDO> deliveryReturnVisitDOList;


    public String getId() {
        return id;
    }

    public DeliveryReturnVisitDO setId(String id) {
        this.id = id;
        return this;
    }

    public String getServiceNoId() {
        return serviceNoId;
    }

    public DeliveryReturnVisitDO setServiceNoId(String serviceNoId) {
        this.serviceNoId = serviceNoId;
        return this;
    }

    public String getCommentedId() {
        return commentedId;
    }

    public DeliveryReturnVisitDO setCommentedId(String commentedId) {
        this.commentedId = commentedId;
        return this;
    }

    public String getCommentScore() {
        return commentScore;
    }

    public DeliveryReturnVisitDO setCommentScore(String commentScore) {
        this.commentScore = commentScore;
        return this;
    }

    public String getCommentMore() {
        return commentMore;
    }

    public DeliveryReturnVisitDO setCommentMore(String commentMore) {
        this.commentMore = commentMore;
        return this;
    }

    public LocalDateTime getCommentDate() {
        return commentDate;
    }

    public DeliveryReturnVisitDO setCommentDate(LocalDateTime commentDate) {
        this.commentDate = commentDate;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public DeliveryReturnVisitDO setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public DeliveryReturnVisitDO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public DeliveryReturnVisitDO setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public String getTypeId() {
        return typeId;
    }

    public DeliveryReturnVisitDO setTypeId(String typeId) {
        this.typeId = typeId;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public DeliveryReturnVisitDO setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public String getCreateName() {
        return createName;
    }

    public DeliveryReturnVisitDO setCreateName(String createName) {
        this.createName = createName;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public DeliveryReturnVisitDO setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getHistory() {
        return history;
    }

    public DeliveryReturnVisitDO setHistory(String history) {
        this.history = history;
        return this;
    }

    public String getPostType() {
        return postType;
    }

    public DeliveryReturnVisitDO setPostType(String postType) {
        this.postType = postType;
        return this;
    }

    public String getCommentFlag() {
        return commentFlag;
    }

    public DeliveryReturnVisitDO setCommentFlag(String commentFlag) {
        this.commentFlag = commentFlag;
        return this;
    }

    public String getPicId() {
        return picId;
    }

    public DeliveryReturnVisitDO setPicId(String picId) {
        this.picId = picId;
        return this;
    }

    public List<DeliveryReturnVisitDO> getDeliveryReturnVisitDOList() {
        return deliveryReturnVisitDOList;
    }

    public DeliveryReturnVisitDO setDeliveryReturnVisitDOList(List<DeliveryReturnVisitDO> deliveryReturnVisitDOList) {
        this.deliveryReturnVisitDOList = deliveryReturnVisitDOList;
        return this;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public DeliveryReturnVisitDO setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }
}
