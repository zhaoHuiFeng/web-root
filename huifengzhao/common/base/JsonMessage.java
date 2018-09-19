package com.huifengzhao.common.base;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author huifengzhao
 * @ClassName JsonMessage
 * @date 2018/9/5
 */
public class JsonMessage implements Serializable {
    public static final String CODE = "code";
    public static final String MSG = "msg";
    public static final String DATA = "data";
    public static final String COUNT = "count";
    private int code;
    private String msg;
    private Object data;
    private int count;

    public JsonMessage(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonMessage(int code, String msg, int count, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.count = count;
    }

    public JsonMessage(int code, String msg) {
        this(code, msg, null);
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public JSONObject toJson() {
        return (JSONObject) JSONObject.toJSON(this);
    }

    public String toFormatJson() {
        return JSONObject.toJSONString(this, SerializerFeature.PrettyFormat);
    }

    public static JSONObject toJson(int code, String msg) {
        return toJson(code, msg, null);
    }

    public static JSONObject toJson(int code, String msg, Object data) {
        JsonMessage jsonMessage = new JsonMessage(code, msg, data);
        return jsonMessage.toJson();
    }

    public static JSONObject toJson(int code, String msg, int count, Object data) {
        JsonMessage jsonMessage = new JsonMessage(code, msg, count, data);
        return jsonMessage.toJson();
    }

    public static String getString(int code, String msg) {
        return getString(code, msg, null);
    }

    public static String getString(int code, String msg, Object data) {
        return toJson(code, msg, data).toString();
    }

    public static String getString(int code, String msg, int count, Object data) {
        return toJson(code, msg, count, data).toString();
    }

    static {
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
    }
}
