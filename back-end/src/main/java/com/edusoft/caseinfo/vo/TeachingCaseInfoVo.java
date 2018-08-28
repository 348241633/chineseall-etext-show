package com.edusoft.caseinfo.vo;

import com.edusoft.caseinfo.model.CaseDetailFile;
import com.edusoft.caseinfo.model.TeachingCaseInfo;

import java.util.List;

/**
 * Created by lego-jspx01 on 2018/8/24.
 */
public class TeachingCaseInfoVo extends TeachingCaseInfo {
    private List<CaseDetailFile> caseDetailFile;

    public List<CaseDetailFile> getCaseDetailFile() {
        return caseDetailFile;
    }

    public void setCaseDetailFile(List<CaseDetailFile> caseDetailFile) {
        this.caseDetailFile = caseDetailFile;
    }
}
