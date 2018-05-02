package com.stylefeng.guns.core.util;

public final class ResponseCode {

    // 响应成功
    public static final int SUCCESS = 200;

    // 请求失败
    public static final int FAIL = 400;

    // 无访问权限
    public static final int FORBIDDEN = 403;

    // 服务内部错误
    public static final int SERVER_ERROR = 500;

    // 请求被过滤掉，不予以处理
    public static final int IGNORED = 600;

    private ResponseCode() {
    }
}
