package com.gizwits.demo.wechat.common.utils;

import com.gizwits.demo.wechat.common.beans.WxEventDeviceBind;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Created by matt on 16-5-25.
 */
public class XMLUtilTest {

    String xml;

    @Before
    public void setUp() throws Exception {
        xml  =
            "<xml>" +
                "<ToUserName><![CDATA[gh_1ad37453ece0]]></ToUserName>" +
                "<FromUserName><![CDATA[o9S82ww4Ur79p8xDFNHk06fvs_ew]]></FromUserName>" +
                "<CreateTime>1464174079</CreateTime>" +
                "<MsgType><![CDATA[device_event]]></MsgType>" +
                "<Event><![CDATA[bind]]></Event>" +
                "<DeviceType><![CDATA[gh_1ad37453ece0]]></DeviceType>" +
                "<DeviceID><![CDATA[5CCF7F800F63]]></DeviceID>" +
                "<Content><![CDATA[]]></Content>" +
                "<SessionID>0</SessionID>" +
                "<OpenID><![CDATA[o9S82ww4Ur79p8xDFNHk06fvs_ew]]></OpenID>" +
            "</xml>";

        xml =
            "<xml>" +
                "<ToUserName><![CDATA[gh_1ad37453ece0]]></ToUserName>" +
                "<FromUserName><![CDATA[o9S82ww4Ur79p8xDFNHk06fvs_ew]]></FromUserName>" +
                "<CreateTime>1464232324</CreateTime>" +
                "<MsgType><![CDATA[device_event]]></MsgType>" +
                "<Event><![CDATA[unsubscribe_status]]></Event>" +
                "<DeviceType><![CDATA[gh_1ad37453ece0]]></DeviceType>" +
                "<DeviceID><![CDATA[5CCF7F800F63]]></DeviceID>" +
                "<OpType>0</OpType>" +
                "<OpenID><![CDATA[o9S82ww4Ur79p8xDFNHk06fvs_ew]]></OpenID>" +
            "</xml>";
    }

    @Test
    public void getWxMsg() throws Exception {
        WxEventDeviceBind deviceInfo = (WxEventDeviceBind) XMLUtil.getWxMsg(WxEventDeviceBind.class, xml);
        System.out.println(deviceInfo);
        System.out.println(XMLUtil.toXML(deviceInfo));
    }

    @Test
    public void XML2Map() throws Exception {
        System.out.println(XMLUtil.XML2Map(xml));
    }
}