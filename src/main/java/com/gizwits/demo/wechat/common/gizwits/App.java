package com.gizwits.demo.wechat.common.gizwits;


import com.gizwits.wechat.sdk.DeviceInfo;
import com.gizwits.wechat.sdk.GizwitsException;
import com.gizwits.wechat.sdk.OpenApi;

import java.util.List;

/**
 * Created by matt on 16-5-20.
 */
public class App {

    private String app_id;
    private String app_secret;
    private String product_key;
    private String product_Secret;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_secret() {
        return app_secret;
    }

    public void setApp_secret(String app_secret) {
        this.app_secret = app_secret;
    }

    public String getProduct_key() {
        return product_key;
    }

    public void setProduct_key(String product_key) {
        this.product_key = product_key;
    }

    public String getProduct_Secret() {
        return product_Secret;
    }

    public void setProduct_Secret(String product_Secret) {
        this.product_Secret = product_Secret;
    }

    public String getBaseApiUrl() {
        return OpenApi.getGizwitsBaseApiUrl();
    }

    public void setBaseApiUrl(String url) {
        OpenApi.setGizwitsBaseApiUrl(url);
    }

    public DeviceInfo bindDevice(String OpenId, String mac) throws GizwitsException {
        return bindDevice(OpenId, mac, "", "");
    }

    public DeviceInfo bindDevice(String OpenId, String mac, String deviceAlias, String deviceRemark) throws GizwitsException {
        return OpenApi.bindDevice(OpenId, app_id, product_key, product_Secret, mac, deviceAlias, deviceRemark);
    }

    public List<DeviceInfo> getBoundDevices(String OpenId) throws GizwitsException {
        return OpenApi.getBoundDevices(OpenId, app_id);
    }

    public boolean getDeviceOnlineStatus(String OpenId, String deviceId) throws GizwitsException {
        return OpenApi.getDeviceOnlineStatus(OpenId, app_id, deviceId);
    }

    public boolean unbindDevice(String OpenId, String deviceId) throws GizwitsException {
        return OpenApi.unbindDevice(OpenId, app_id, deviceId);
    }

    public boolean unbindDeviceByMac(String OpenId, String Mac) throws GizwitsException {
        List<DeviceInfo> list = getBoundDevices(OpenId);
        for (DeviceInfo device : list) {
            if (device.getMac().equalsIgnoreCase(Mac)) {
                return OpenApi.unbindDevice(OpenId, app_id, device.getDid());
            }
        }
        return false;
    }
}
