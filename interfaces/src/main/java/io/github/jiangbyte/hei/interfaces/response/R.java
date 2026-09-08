package io.github.jiangbyte.hei.interfaces.response;

import java.io.Serializable;

/**
 * 统一 API 响应包装。
 *
 * @param <T> 业务数据类型
 */
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 业务状态码，成功一般为 0 或 "OK" */
    private String code;

    /** 提示信息 */
    private String message;

    /** 业务数据 */
    private T data;

    public R() {
    }

    public R(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应（无数据）。
     */
    public static <T> R<T> ok() {
        return new R<>("0", "success", null);
    }

    /**
     * 成功响应（带数据）。
     */
    public static <T> R<T> ok(T data) {
        return new R<>("0", "success", data);
    }

    /**
     * 失败响应。
     */
    public static <T> R<T> fail(String code, String message) {
        return new R<>(code, message, null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
