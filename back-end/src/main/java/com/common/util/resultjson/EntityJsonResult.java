package com.common.util.resultjson;

/**
 * Created by lego-jspx01 on 2016/5/19.
 */
public class EntityJsonResult extends JsonResult {
    Object data;

    public EntityJsonResult() {
    }

    public EntityJsonResult(String status, String message) {
        this.message = message;
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
