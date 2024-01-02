package com.ruoyi.flow.notifier.sender;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.flow.dto.FlowNoticeInfo;
import com.ruoyi.flow.notifier.NoticeSender;
import com.ruoyi.system.api.domain.SysUser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import java.net.URLEncoder;

/**
 * dingtalk
 *
 * @author H2Ocean
 * @date 2023/04/13 14:21
 **/

@Data
@Component
@Slf4j
public class DingtalkSender implements NoticeSender {

    @Value("${noticeSender.dingTalk.webhook}")
    private String webhook;
    @Value("${noticeSender.dingTalk.key}")
    private String key;


    @Override
    public String getNoticeType() {
        return "dingding";
    }

    @Override
    public void sendNotice(FlowNoticeInfo flowNoticeInfo) {

        List<SysUser> assignees = flowNoticeInfo.getAssignees();
        List<String> phonenumbers = new ArrayList<>();
        assignees.forEach(item -> {
            String phonenumber = item.getPhonenumber();
            if (phonenumber != null) {
                phonenumbers.add(phonenumber);
            }
        });

        Map<String, Object> json = new HashMap();
        Map<String, Object> text = new HashMap();
        Map<String, Object> at = new HashMap();
        json.put("msgtype", "text");
        text.put("content", flowNoticeInfo.getMessage());
        json.put("text", text);
        //@具体的人
        at.put("atMobiles", phonenumbers);
        json.put("at", at);

        try {
            String url = concatenateUrl(webhook, key);
            //发送post请求
            sendPostByMap(url, json);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String sendPostByMap(String url, Map<String, Object> mapParam) {
        Map<String, String> headParam = new HashMap();
        headParam.put("Content-type", "application/json;charset=UTF-8");
        return sendPost(url, mapParam, headParam);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, Object> param, Map<String, String> headParam) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性 请求头
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Fiddler");

            if (headParam != null) {
                for (Map.Entry<String, String> entry : headParam.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(JSON.toJSONString(param));
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.info("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public String concatenateUrl(String webhook, String key) throws Exception {

        Long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + key;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
        String url = webhook+"&timestamp="+timestamp+"&sign="+sign;

        return url;
    }

}
