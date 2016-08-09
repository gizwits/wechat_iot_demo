package com.gizwits.demo.wechat.handler;

import com.gizwits.demo.wechat.common.utils.XMLUtil;
import com.gizwits.demo.wechat.service.DeviceService;
import com.gizwits.demo.wechat.service.TextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by matt on 16-5-23.
 * 处理所有的消息（事件/普通）
 * 将结果返回微信服务器
 */
@Service
public class MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private TextService textService;

    public String processRequest(HttpServletRequest request) {

        logger.info(" -------->  开始处理消息:::");

        // 我们这里只处理设备绑定事件
        InputStream is = null;
        String result = "success";
        try
        {
            is = request.getInputStream();
            String xml = XMLUtil.InputStream2String(is);

            logger.info(" -------->  收到消息： \n{}\n", xml);

            Map msg = XMLUtil.XML2Map(xml);
            logger.info(" -------->  收到信息：\n{}\n", msg);

            String msgType = (String)msg.get("MsgType");
            switch (msgType) {
                case "device_event":
                    logger.info(" -------->  解析为设备事件");
                    deviceService.handle(xml);
                    break;
                case "text":
                    logger.info(" -------->  解析为文本消息");
                    result = textService.handle(xml);

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        logger.info("\n-------->  处理完信息::{}::: --------------------------" +
                "-------------------------------------", result);

        return result;
    }
}
