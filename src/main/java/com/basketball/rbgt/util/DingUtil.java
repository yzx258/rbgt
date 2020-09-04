package com.basketball.rbgt.util;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @company：厦门宜车时代信息技术有限公司
 * @copyright：Copyright (C), 2020
 * @author：yucw
 * @date：2020/9/4 17:48
 * @version：1.0
 * @description: 钉钉发送消息
 */
@Component
@Slf4j
public class DingUtil {

    /**
     * 钉钉机器人地址
     */
    private final static String URL = "https://oapi.dingtalk.com/robot/send?access_token=65e76ea682a9335c8416f09e67b39cda6cbd92aa9789fa85058c32f528c1e55c";

    /**
     * 发送钉钉机器人消息
     * @param msg
     */
    public void sendMassage(String msg){
        String text = "{\"msgtype\": \"text\",\"text\": {\"content\": \"【喜讯】"+msg+"\"}}";
        HttpUtil.post(URL,text);
    }

    public static void main(String[] args) {
        String mssages = "\\rBS : 8 场\\rHL : 80 元";
        String text = "{\"msgtype\": \"text\",\"text\": {\"content\": \"【喜讯】"+mssages+"\"}}";
        HttpUtil.post(URL,text);
    }

}
