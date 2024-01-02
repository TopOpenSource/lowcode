package com.ruoyi.flow;

import com.ruoyi.LowCodeApplication;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.dto.CommonDto;
import com.ruoyi.dynamicform.service.DynamicFormService;
import com.ruoyi.flow.domain.FlowObject;
import com.ruoyi.flow.dto.FlowNoticeInfo;
import com.ruoyi.flow.mapper.FlowObjectMapper;
import com.ruoyi.flow.notifier.SenderFactory;
import com.ruoyi.flow.notifier.sender.DingtalkSender;
import com.ruoyi.flow.service.impl.WorkFlowServiceImpl;
import com.ruoyi.form.domain.FormObjectAttribute;
import com.ruoyi.form.mapper.base.ObjectAttributeBaseDao;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.domain.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = LowCodeApplication.class)
public class NoticeTest {
    @Autowired
    private SenderFactory senderFactory;

    @Autowired
    private RemoteUserService userService;

    @Autowired
    private DingtalkSender dingtalkSender;

    @Autowired
    private FlowObjectMapper bpmnMapper;

    @Autowired
    private ObjectAttributeBaseDao objectAttributeBaseDao;

    @Autowired
    private DynamicFormService dynamicFormService;

    @Autowired
    private WorkFlowServiceImpl workFlowService;

    @Test
    public void testSender(){
        FlowNoticeInfo flowNoticeInfo = new FlowNoticeInfo();

        flowNoticeInfo.setMessage("abc");


        senderFactory.getSender("email",flowNoticeInfo);


    }

    @Test
    public void getUser() {
        R<SysUser> hzy = userService.getUserByName("admin", SecurityConstants.INNER);
        SysUser data = hzy.getData();
        System.out.println(data);
    }

    @Test
    public void dingTalkAT() throws Exception {

        String s = workFlowService.mesCreator("cf972815-da72-11ed-84a6-8cec4b2fe64f", 1096264173250228224l);

        List<String> phonenumbers = new ArrayList<>();
        phonenumbers.add("19861809200");

        Map<String, Object> json = new HashMap();
        Map<String, Object> text = new HashMap();
        Map<String, Object> at = new HashMap();
        json.put("msgtype", "text");
        text.put("content", s);
        json.put("text", text);
        //@具体的人
        at.put("atMobiles", phonenumbers);
        json.put("at", at);


        String url = dingtalkSender.concatenateUrl("https://oapi.dingtalk.com/robot/send?access_token=2828bb2c67190e2cc313e44eaa541f82e705e0552d1a28bdd032e012bafcfcb5","SECbdd0e4e9e406db7bb9e6ffbecf544088059082ef418ce09eb9064de74f782f8a");

        dingtalkSender.sendPostByMap(url,json);
    }

    @Test
    public void test(){
//        FlowObject flowObject = bpmnMapper.getByProcDefId("process_on2nfx2c:52:be83f8a4-da72-11ed-84a6-8cec4b2fe64f");
//        System.out.println(flowObject.getFormObjectId());
        List<FormObjectAttribute> formObjectAttributes = objectAttributeBaseDao.selectByObjectId(1096264173250228224l);
        CommonDto commonDto = dynamicFormService.selectByPk(1096264173250228224l, 1096272854192828416l, formObjectAttributes);
        System.out.println(commonDto.get("id"));
    }

}
