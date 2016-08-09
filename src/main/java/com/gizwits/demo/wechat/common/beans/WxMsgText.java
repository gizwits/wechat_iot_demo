package com.gizwits.demo.wechat.common.beans;

import com.gizwits.demo.wechat.common.utils.XMLUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by matt on 16-5-26.
 */
@XStreamAlias("xml")
public class WxMsgText extends WxBaseMsg {

    private String Content;
    private String MsgId;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String msgId) {
        MsgId = msgId;
    }

    public String getReply() {
        String xml = XMLUtil.toXML(this);
        return xml;
    }
}
