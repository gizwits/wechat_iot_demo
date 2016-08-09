package com.gizwits.demo.wechat.handler;

import com.gizwits.demo.wechat.common.SysCons;
import com.gizwits.demo.wechat.common.enums.ErrCode;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.json.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by matt on 16-5-11.
 * TokenManager负责管理微信的AccessToken，
 * 平时获取Token时，都需要从TokenManager中获取
 */
@Service
public class TokenManager {

    private static final Logger logger = LoggerFactory.getLogger(TokenManager.class);


    @Autowired
    private SysCons sysCons;

    private String access_token;
    private Long expired_timestamp;
    private Long offset = 5000l;

    /**
     * 获取当前有效Token
     * @return
     */
    public String getAccessToken()
    {

        if (null == expired_timestamp || null == access_token)
        {
            renewAccessToken();
        }

        // 如果Token过期了
        if (System.currentTimeMillis() > expired_timestamp + offset)
        {
            // 更新Token
            renewAccessToken();
        }

        return access_token;
    }

    public void renewAccessToken()
    {
        //请求微信服务器
        HttpRequest request = HttpRequest
                .get("https://api.weixin.qq.com/cgi-bin/access_token")
                .query("grant_type", "client_credential")
                .query("appid", sysCons.APPID)
                .query("secret", sysCons.APPSECRET);

        HttpResponse response = request.send();

        Map resp_body = new JsonParser().parse(response.body());

        if (resp_body.containsKey("errcode"))
        {
            Integer errcode = (Integer) resp_body.get("errcode");
            String errmsg = (String) resp_body.get("errmsg");
            logger.error("刷新Token -----> 请求失败 [ {}: {}]", errcode, errmsg);
            throw new RuntimeException("获取Token失败，请检查appid或appsecret或url是否正确；错误码【" + errcode + "】错误信息【" + ErrCode.searchErrMsg(errcode) + "】");
        }

        logger.info("刷新Token -----> 请求成功 [ {} ]", response.body());
        // 刷新Token的值
        access_token = (String) resp_body.get("access_token");
        expired_timestamp = System.currentTimeMillis() + (Integer) resp_body.get("expires_in");
    }

    public String getOpenIdByCode(String code) {
        //请求微信服务器
        HttpRequest request = HttpRequest
                .get("https://api.weixin.qq.com/sns/oauth2/access_token")
                .query("appid", sysCons.APPID)
                .query("secret", sysCons.APPSECRET)
                .query("code", code)
                .query("grant_type", "authorization_code");

        HttpResponse response = request.send();
        Map resp_body = new JsonParser().parse(response.body());

        if (resp_body.containsKey("openid")) {
            return (String) resp_body.get("openid");
        }

        return null;
    }

    @Override
    public String toString()
    {
        return "TokenManager{" +
                "access_token='" + access_token + '\'' +
                ", expired_timestamp=" + expired_timestamp +
                ", offset=" + offset +
                '}';
    }
}
