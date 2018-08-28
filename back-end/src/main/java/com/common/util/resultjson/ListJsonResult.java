package com.common.util.resultjson;

import java.util.List;

/**
 * Created by eric on 2016-5-16.
 * 系统返回前台json格式对象
 */
public class ListJsonResult  extends JsonResult{
    long total;
    List<?> data;

    public ListJsonResult() {
    }

    public ListJsonResult(String status, String message) {
        this.message = message;
        this.status = status;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
