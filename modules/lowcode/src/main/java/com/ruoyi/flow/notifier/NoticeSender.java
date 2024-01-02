package com.ruoyi.flow.notifier;

import com.ruoyi.flow.dto.FlowNoticeInfo;

/**
 * NoticeSender
 *
 * @author H2Ocean
 * @date 2023/04/13 15:19
 **/
public interface NoticeSender {

    /**
     * 获取通知类型
     *
     * @return java.lang.String
     * @author H2Ocean
     * @date 2023/04/13 15:17
     */
    String getNoticeType();

    /**
     * 发送通知
     *
     * @param flowNoticeInfo
     * @return void
     * @author H2Ocean
     * @date 2023/04/13 15:17
     */
    void sendNotice(FlowNoticeInfo flowNoticeInfo);
}
