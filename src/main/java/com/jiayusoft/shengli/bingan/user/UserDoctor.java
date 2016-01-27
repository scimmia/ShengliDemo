package com.jiayusoft.shengli.bingan.user;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by ASUS on 2014/11/19.
 */
public class UserDoctor implements java.io.Serializable {

    // Fields

    private Long id;
    private String name;
    private String idcard;
    private String password;
    private String glksbm;
    private String cxlb;
    private String yyidentiry;
    private String officecode;
    private Long bnjyyxq;
    private Long jyyxq;
    private String logoName;
    private String logoColor;

// Constructors

    /** default constructor */
    public UserDoctor() {
        String temp = "";
        this.name = temp;
        this.idcard = temp;
        this.password = temp;
        this.glksbm = temp;
        this.cxlb = temp;
        this.yyidentiry = temp;
        this.officecode = temp;
    }

    // Property accessors

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (StringUtils.isNotEmpty(name))
            this.name = name;
    }

    public String getIdcard() {
        return this.idcard;
    }

    public void setIdcard(String idcard) {
        if (StringUtils.isNotEmpty(idcard))
            this.idcard = idcard;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        if (StringUtils.isNotEmpty(password))
            this.password = password;
    }

    public String getGlksbm() {
        return this.glksbm;
    }

    public void setGlksbm(String glksbm) {
        if (StringUtils.isNotEmpty(glksbm))
            this.glksbm = glksbm;
    }

    public String getCxlb() {
        return cxlb;
    }

    public void setCxlb(String cxlb) {
        if (StringUtils.isNotEmpty(cxlb))
            this.cxlb = cxlb;
    }

    public String getYyidentiry() {
        return yyidentiry;
    }

    public void setYyidentiry(String yyidentiry) {
        if (StringUtils.isNotEmpty(yyidentiry))
            this.yyidentiry = yyidentiry;
    }

    public String getOfficecode() {
        return officecode;
    }

    public void setOfficecode(String officecode) {
        if (StringUtils.isNotEmpty(officecode))
            this.officecode = officecode;
    }

    public Long getBnjyyxq() {
        return bnjyyxq;
    }

    public void setBnjyyxq(Long bnjyyxq) {
        this.bnjyyxq = bnjyyxq;
    }

    public Long getJyyxq() {
        return jyyxq;
    }

    public void setJyyxq(Long jyyxq) {
        this.jyyxq = jyyxq;
    }

    public String getLogoName() {
        return logoName;
    }

    public void setLogoName(String logoName) {
        this.logoName = logoName;
    }

    public String getLogoColor() {
        return logoColor;
    }

    public void setLogoColor(String logoColor) {
        this.logoColor = logoColor;
    }
}