package com.common.util.resultjson;

/**
 * Created by lego-jspx01 on 2016/5/20.
 */
public class ValidateJsonResult {
    String message;
    String status;
    String result;
    public ValidateJsonResult() {
    }

    public ValidateJsonResult(String status, String message) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
