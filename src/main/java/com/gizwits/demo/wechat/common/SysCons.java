package com.gizwits.demo.wechat.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by matt on 16-5-11.
 */
@Component
public class SysCons {
    @Value("#{sys.token}")
    public String TOKEN;
    @Value("#{sys.appID}")
    public String APPID;
    @Value("#{sys.appsecret}")
    public String APPSECRET;

    @Override
    public String toString() {
        return "SysCons{" +
                "TOKEN='" + TOKEN + '\'' +
                ", APPID='" + APPID + '\'' +
                ", APPSECRET='" + APPSECRET + '\'' +
                '}';
    }
}
