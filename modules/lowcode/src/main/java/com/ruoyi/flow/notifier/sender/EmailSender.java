package com.ruoyi.flow.notifier.sender;

import com.ruoyi.flow.dto.FlowNoticeInfo;
import com.ruoyi.flow.notifier.NoticeSender;
import com.ruoyi.system.api.domain.SysUser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * email
 *
 * @author H2Ocean
 * @date 2023/04/13 14:22
 **/
@Data
@Component
@Slf4j
public class EmailSender implements NoticeSender {

    @Value("${noticeSender.email.sender}")
    private String sender;
    @Value("${noticeSender.email.code}")
    private String code;
    @Value("${noticeSender.email.smtp.host}")
    private String host;
    @Value("${noticeSender.email.smtp.port}")
    private int port;

    @Override
    public String getNoticeType() {
        return "email";
    }

    @Override
    public void sendNotice(FlowNoticeInfo flowNoticeInfo) {

        List<SysUser> assignees = flowNoticeInfo.getAssignees();
        List<String> emails = new ArrayList<>();
        assignees.forEach(item -> {
            String email = item.getEmail();
            if (email != null) {
                emails.add(email);
            }
        });

        try {
            sendEmail(sender, "审批", flowNoticeInfo.getMessage(), code, emails);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendEmail(String sender, String title, String text, String senderCode, List<String> addresses) throws MessagingException {

        // 配置类
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.debug", "true");
        // 获取会话对象
        Session session = Session.getInstance(properties);
        // 获取邮件对象
        Message message = new MimeMessage(session);


        // 设置发件人邮箱地址
        message.setFrom(new InternetAddress(sender));
        // 收件人
        message.setRecipients(Message.RecipientType.TO, addresses(addresses));
        // 设置邮件标题
        message.setSubject(title);
        // 设置邮件内容
        message.setText(text);


        // 得到邮差对象
        Transport transport = session.getTransport();
        // 连接自己的邮箱账户(邮箱，授权码)
        transport.connect(sender, senderCode);
        // 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    public InternetAddress[] addresses(List<String> addressee) throws MessagingException {
        InternetAddress[] internetAddressList = new InternetAddress[addressee.size()];
        for (int i = 0; i < addressee.size(); i++) {
            InternetAddress internetAddress = new InternetAddress(addressee.get(i));
            internetAddressList[i] = internetAddress;
        }
        return internetAddressList;
    }
}
