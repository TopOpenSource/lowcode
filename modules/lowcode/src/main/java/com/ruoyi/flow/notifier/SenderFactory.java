package com.ruoyi.flow.notifier;

import com.ruoyi.flow.dto.FlowNoticeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * factory
 *
 * @author H2Ocean
 * @date 2023/04/12 21:56
 **/
@Component
public class SenderFactory {

    @Autowired
    private List<NoticeSender> senders;

    private Map<String, NoticeSender> map = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        map = new HashMap<>();
        senders.forEach(sender -> {
            map.put(sender.getNoticeType(), sender);
        });
    }

    public void getSender(String noticeType, FlowNoticeInfo flowNoticeInfo) {
        this.map.get(noticeType).sendNotice(flowNoticeInfo);
    }
}
