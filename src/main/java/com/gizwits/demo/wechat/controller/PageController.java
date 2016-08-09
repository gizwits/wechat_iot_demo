package com.gizwits.demo.wechat.controller;

import com.gizwits.demo.wechat.common.gizwits.App;
import com.gizwits.demo.wechat.handler.TokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by matt on 16-5-25.
 */
@Controller
public class PageController {

    private final Logger logger = LoggerFactory.getLogger(PageController.class);

    @Autowired
    TokenManager tokenManager;
    @Autowired
    App app;

    @RequestMapping("/")
    public String index() {
        logger.info(">>>>>>>>> 进入首页");
        return "index";
    }

    @RequestMapping("/device")
    public String device(String code, ModelMap model) {
        String openId = tokenManager.getOpenIdByCode(code);
        logger.info(">>>>>>>>> 设备列表: OpenId:: {}", openId);
        model.put("OpenId", openId);
        model.put("gizwitsAppId", app.getApp_id());
        return "device_list";
    }

    @RequestMapping("/device_detail")
    public String deviceDetail(String did, String openid, ModelMap model) {
        logger.info(">>>>>>>>> 管理设备: OpenId:: {}", openid);
        model.put("OpenId", openid);
        model.put("did", did);
        model.put("gizwitsAppId", app.getApp_id());
        return "device";
    }

}