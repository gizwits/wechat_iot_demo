package com.gizwits.demo.wechat.common.utils;

/**
 * Created by matt on 16-5-26.
 */
public class StrUtil {
    public static boolean isBlank(String alias) {
        return null == alias || alias.trim().equals("");
    }
}
