package com.gizwits.demo.wechat.common.gizwits;

import com.gizwits.wechat.sdk.DeviceInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by matt on 16-5-27.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:spring-beans-test.xml")
//public class AppTest {
//
//    @Autowired
//    private App app;
//
//    @Test
//    public void getBoundDevices() throws Exception {
//
//        List<DeviceInfo> list = app.getBoundDevices("o9S82ww4Ur79p8xDFNHk06fvs_ew");
//
//        System.out.println("当前绑定设备数：" + list.size());
//
//        for (DeviceInfo info : list) {
//            System.out.println(info.getDid() + ";" + info.getMac());
//        }
//    }
//
//    @Test
//    public void unbindDeviceByMac() throws Exception {
//        System.out.println(
//                app.unbindDeviceByMac("o9S82ww4Ur79p8xDFNHk06fvs_ew", "5ccf7f800f63")
//        );
//    }
//
//    @Test
//    public void unbindDevice() throws Exception {
//        app.unbindDevice("o9S82ww4Ur79p8xDFNHk06fvs_ew", "ZuSmCgVKmjUV2zKwX5xMoX");
//        app.unbindDevice("o9S82ww4Ur79p8xDFNHk06fvs_ew", "EwuiGweBxHappAW5b4LsQq");
//    }
//
//}