package com.edusoft.caseinfo.model;

import java.io.Serializable;

public class CaseDetailFileKey implements Serializable {
    private Integer id;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}