package com.gizwits.demo.wechat.service;

import com.gizwits.demo.wechat.common.beans.WxMsgText;
import com.gizwits.demo.wechat.common.utils.StrUtil;
import com.gizwits.demo.wechat.common.utils.XMLUtil;
import com.gizwits.wechat.sdk.DeviceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by matt on 16-5-26.
 */
@Service
public class TextService {

    private final Logger logger = LoggerFactory.getLogger(TextService.class);

    @Autowired
    DeviceService deviceService;

    public String handle(String xml) {

        WxMsgText text = (WxMsgText) XMLUtil.getWxMsg(WxMsgText.class, xml);

        WxMsgText replyMsg = null;
        switch (text.getContent()) {
            case "设备列表":
                String openId = text.getFromUserName();
                replyMsg = toReplyDevineList(
                        deviceService.getDeviceList(openId),
                        text);
                logger.info(" -------->  获取设备列表：\n{}\n", replyMsg);
                break;
        }

        // 将数据，以xml形式，返回
        return replyMsg != null ? replyMsg.getReply() : "success";
    }


    public WxMsgText toReplyDevineList(List<DeviceInfo> list, WxMsgText recieveMsg) {
        String content = list.size() == 0 ? "No Device" : "";
        for (DeviceInfo deviceInfo : list) {
            content += device2String(deviceInfo) + "\n";
        }

        String openid = recieveMsg.getFromUserName();
        recieveMsg.setFromUserName(recieveMsg.getToUserName());
        recieveMsg.setToUserName(openid);
        recieveMsg.setCreateTime(System.currentTimeMillis());
        recieveMsg.setContent(content.trim());
        return recieveMsg;
    }

    public String device2String(DeviceInfo deviceInfo) {
        return "Device" + (StrUtil.isBlank(deviceInfo.getAlias()) ? "" : "[" + deviceInfo.getAlias() + "]") +
                "[" + (deviceInfo.getIsOnline() ? "Online" : "Offline") + "]: " + deviceInfo.getMac();
    }
}
