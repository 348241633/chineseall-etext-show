package com.common.util.resultjson;

/**
 * Created by lego-jspx01 on 2016/5/20.
 */
public class WsJsonResult {
    String state;
    String msg;
    public WsJsonResult() {
    }

    public WsJsonResult(String state, String msg) {
        this.msg = msg;
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
