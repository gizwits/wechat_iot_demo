package com.gizwits.demo.wechat.service;

import com.gizwits.demo.wechat.common.SysCons;
import com.gizwits.demo.wechat.common.utils.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by matt on 16-5-27.
 */
@Service
public class SignatureService {

    @Autowired
    SysCons sysCons;

    public Map getSignatureInfo() {
        Map map = new HashMap();
        map.put("appId", sysCons.APPID);
        map.put("timestamp", System.currentTimeMillis() / 1000);
        map.put("nonceStr", SignUtil.getRandomString(30));
        return map;
    }

}
