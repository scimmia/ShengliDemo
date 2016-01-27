package com.jiayusoft.shengli.bingan.bingan;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.util.List;

/**
 * Created by Administrator on 15-1-4.
 */
public class BinganQuality {
    Bingan bingan;
    int score;
    List<ImmutableTriple<String,String,Integer>> pingjiaList;

    public BinganQuality(Bingan bingan,int score) {
        this.bingan = bingan;
        this.score = score;
//        pingjiaList = new LinkedList<ImmutableTriple<String, String, Integer>>();
    }

//    public void addPingjia(String xiangmu,String neirong,int score){
//        this.score = this.score - score;
//        pingjiaList.add(new ImmutableTriple<String, String, Integer>(xiangmu,neirong,score));
//    }

    public Bingan getBingan() {
        return bingan;
    }

    public void setBingan(Bingan bingan) {
        this.bingan = bingan;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<ImmutableTriple<String, String, Integer>> getPingjiaList() {
        return pingjiaList;
    }

    public void setPingjiaList(List<ImmutableTriple<String, String, Integer>> pingjiaList) {
        this.pingjiaList = pingjiaList;
    }
}
