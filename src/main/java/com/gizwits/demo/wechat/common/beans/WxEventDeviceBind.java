package com.gizwits.demo.wechat.common.beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by matt on 16-5-24.
 */
@XStreamAlias("xml")
public class WxEventDeviceBind extends WxBaseMsg {

    private String Event;
    private String DeviceType;
    private String DeviceID;
    private String Content;
    private String SessionID;
    private String OpenID;
    private String OpType;


    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String sessionID) {
        SessionID = sessionID;
    }

    public String getOpenID() {
        return OpenID;
    }

    public void setOpenID(String openID) {
        OpenID = openID;
    }

    public String getOpType() {
        return OpType;
    }

    public void setOpType(String opType) {
        OpType = opType;
    }

    @Override
    public String toString() {
        return "WxEventDeviceBind{" +
                "Event='" + Event + '\'' +
                ", DeviceType='" + DeviceType + '\'' +
                ", DeviceID='" + DeviceID + '\'' +
                ", Content='" + Content + '\'' +
                ", SessionID='" + SessionID + '\'' +
                ", OpenID='" + OpenID + '\'' +
                ", OpType='" + OpType + '\'' +
                "} " + super.toString();
    }
}
