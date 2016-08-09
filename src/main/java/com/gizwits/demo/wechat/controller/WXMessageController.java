package com.gizwits.demo.wechat.controller;

import com.gizwits.demo.wechat.common.beans.WeChatVerify;
import com.gizwits.demo.wechat.common.utils.SignUtil;
import com.gizwits.demo.wechat.handler.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by matt on 16-5-16.
 */
@Controller
@RequestMapping("/wx")
public class WXMessageController {

    private Logger logger = LoggerFactory.getLogger(WXMessageController.class);

    @Autowired
    private SignUtil signUtil;
    @Autowired
    private MessageHandler messageHandler;

    /**
     * 校验信息是否是从微信服务器发过来的。
     *
     * @param weChatVerify
     */
    @RequestMapping(value = "/api",method = RequestMethod.GET)
    @ResponseBody
    public String valid(WeChatVerify weChatVerify) {

        logger.info("验证请求：{}", weChatVerify.toString());

        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (signUtil.checkSignature(weChatVerify))
        {
            logger.info("验证通过！");
            return weChatVerify.getEchostr();
        }
        else
        {
            logger.warn("不是微信服务器发来的请求,请小心!");
            return null;
        }
    }

    /**
     * 微信消息的处理
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/api",method = RequestMethod.POST)
    @ResponseBody
    public String dispose(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /* 消息的接收、响应、处理 */
        logger.info(">>>>>>>>> 发送请求");

        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 调用核心业务类接收消息、处理消息
        return messageHandler.processRequest(request);
    }

}
