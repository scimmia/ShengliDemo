package com.jiayusoft.shengli.bingan.bingan;


import org.apache.commons.lang3.StringUtils;

/**
 * Entity mapped to table BINGAN.
 */
public class Bingan {

    private String biaoshima;
    private String binganhao;
    private String zuzhidaima;
    private String zuzhiname;
    private String fenzhijigoubiaosi;
    private String xingming;
    private String chuyuankeshi;
    private String chuyuanshijian;
    private String zhusu;
    private Boolean shoucang;
    private Boolean jieyue;
    private String baomijibie;
//    public Bingan() {
//    }

    public Bingan(String biaoshima) {
        this.biaoshima = biaoshima;
        String temp = "";
        this.binganhao = temp;
        this.zuzhidaima = temp;
        this.zuzhiname = temp;
        this.fenzhijigoubiaosi = temp;
        this.xingming = temp;
        this.chuyuankeshi = temp;
        this.chuyuanshijian = temp;
        this.zhusu = temp;
        this.shoucang = false;
        this.jieyue = false;
        this.baomijibie = null;
    }

    public Bingan(String biaoshima, String binganhao, String zuzhidaima, String zuzhiname, String fenzhijigoubiaosi, String xingming, String chuyuankeshi, String chuyuanshijian, String zhusu, Boolean shoucang, Boolean jieyue) {
        this.biaoshima = biaoshima;
        this.binganhao = binganhao;
        this.zuzhidaima = zuzhidaima;
        this.zuzhiname = zuzhiname;
        this.fenzhijigoubiaosi = fenzhijigoubiaosi;
        this.xingming = xingming;
        this.chuyuankeshi = chuyuankeshi;
        this.chuyuanshijian = chuyuanshijian;
        this.zhusu = zhusu;
        this.shoucang = shoucang;
        this.jieyue = jieyue;
    }

    public String getBiaoshima() {
        return biaoshima;
    }

    public void setBiaoshima(String biaoshima) {
        if (StringUtils.isNotEmpty(biaoshima))  this.biaoshima = biaoshima;
    }

    public String getBinganhao() {
        return binganhao;
    }

    public void setBinganhao(String binganhao) {
        if (StringUtils.isNotEmpty(binganhao))   this.binganhao = binganhao;
    }

    public String getZuzhidaima() {
        return zuzhidaima;
    }

    public void setZuzhidaima(String zuzhidaima) {
        if (StringUtils.isNotEmpty(zuzhidaima))
            this.zuzhidaima = zuzhidaima;
    }

    public String getZuzhiname() {
        return zuzhiname;
    }

    public void setZuzhiname(String zuzhiname) {
        if (StringUtils.isNotEmpty(zuzhiname))
            this.zuzhiname = zuzhiname;
    }

    public String getFenzhijigoubiaosi() {
        return fenzhijigoubiaosi;
    }

    public void setFenzhijigoubiaosi(String fenzhijigoubiaosi) {
        if (StringUtils.isNotEmpty(fenzhijigoubiaosi))
            this.fenzhijigoubiaosi = fenzhijigoubiaosi;
    }

    public String getXingming() {
        return xingming;
    }

    public void setXingming(String xingming) {
        if (StringUtils.isNotEmpty(xingming))    this.xingming = xingming;
    }

    public String getChuyuankeshi() {
        return chuyuankeshi;
    }

    public void setChuyuankeshi(String chuyuankeshi) {
        if (StringUtils.isNotEmpty(chuyuankeshi))     this.chuyuankeshi = chuyuankeshi;
    }

    public String getChuyuanshijian() {
        return chuyuanshijian;
    }

    public void setChuyuanshijian(String chuyuanshijian) {
        if (StringUtils.isNotEmpty(chuyuanshijian))     this.chuyuanshijian = chuyuanshijian;
    }


    public String getZhusu() {
        return zhusu;
    }

    public void setZhusu(String zhusu) {
        if (StringUtils.isNotEmpty(zhusu))     this.zhusu = zhusu;
    }

    public Boolean getShoucang() {
        return shoucang;
    }

    public void setShoucang(Boolean shoucang) {
        this.shoucang = shoucang;
    }

    public Boolean getJieyue() {
        return jieyue;
    }

    public void setJieyue(Boolean jieyue) {
        this.jieyue = jieyue;
    }


    public String getBaomijibie() {
        return baomijibie;
    }

    public void setBaomijibie(String baomijibie) {
        this.baomijibie = baomijibie;
    }
}

