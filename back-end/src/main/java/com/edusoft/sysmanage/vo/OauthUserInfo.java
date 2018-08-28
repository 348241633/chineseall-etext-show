package com.edusoft.sysmanage.vo;

import java.util.List;

/**
 * Created by lego-jspx01 on 2018/7/24.
 */
public class OauthUserInfo {
    private Long user_id;
    private String user_name;
    private String login_name;
    private String user_type;
    private String user_source;
    private String sex;
    private String birthday;
    private String tel;
    private String email;
    private String work_no;
    private String qq;
    private String wechat;
    private String sina;
    private String image_url;
    private String token;
    private List<String> subjects;
    private List<String> subjects2;
    private List<String>teach_grades;
    private List<String>teach_grade_ids;
    private List<String> auths;
    private List<String> district_ids;
    private List<String> district_names;
//    private List<OauthOrganInfo> organ;


    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_source() {
        return user_source;
    }

    public void setUser_source(String user_source) {
        this.user_source = user_source;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWork_no() {
        return work_no;
    }

    public void setWork_no(String work_no) {
        this.work_no = work_no;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getSina() {
        return sina;
    }

    public void setSina(String sina) {
        this.sina = sina;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<String> getSubjects2() {
        return subjects2;
    }

    public void setSubjects2(List<String> subjects2) {
        this.subjects2 = subjects2;
    }

    public List<String> getTeach_grades() {
        return teach_grades;
    }

    public void setTeach_grades(List<String> teach_grades) {
        this.teach_grades = teach_grades;
    }

    public List<String> getTeach_grade_ids() {
        return teach_grade_ids;
    }

    public void setTeach_grade_ids(List<String> teach_grade_ids) {
        this.teach_grade_ids = teach_grade_ids;
    }

    public List<String> getAuths() {
        return auths;
    }

    public void setAuths(List<String> auths) {
        this.auths = auths;
    }

    public List<String> getDistrict_ids() {
        return district_ids;
    }

    public void setDistrict_ids(List<String> district_ids) {
        this.district_ids = district_ids;
    }

    public List<String> getDistrict_names() {
        return district_names;
    }

    public void setDistrict_names(List<String> district_names) {
        this.district_names = district_names;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
