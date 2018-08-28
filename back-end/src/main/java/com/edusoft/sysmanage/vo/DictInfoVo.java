package com.edusoft.sysmanage.vo;

import java.io.Serializable;

/**
 * Created by lego-jspx01 on 2016/5/19.
 */
public class DictInfoVo implements Serializable {
    private String name;
    private String value;
    private Integer id;
    private Integer pid;

    public DictInfoVo() {

    }

    public DictInfoVo(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public DictInfoVo(String name, String value, Integer id, Integer pid) {
        this.name = name;
        this.value = value;
        this.id = id;
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}
