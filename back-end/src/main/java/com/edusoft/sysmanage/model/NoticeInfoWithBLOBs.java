package com.edusoft.sysmanage.model;

import java.io.Serializable;

public class NoticeInfoWithBLOBs extends NoticeInfo implements Serializable {
    private String content;

    private static final long serialVersionUID = 1L;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}