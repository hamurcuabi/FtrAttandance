package com.emrehmrc.ftrattendance.utils;

import com.emrehmrc.ftrattendance.model.Detail;
import com.emrehmrc.ftrattendance.model.Member;

import java.util.ArrayList;

public class SingletonCurrentValues {
    private ArrayList<Detail> details;
    private Member member;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public ArrayList<Detail> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<Detail> details) {
        this.details = details;
    }

    private static SingletonCurrentValues singletonCurrentValues=null;

    private SingletonCurrentValues() {

    }
    public static SingletonCurrentValues getInstance(){
        if(singletonCurrentValues==null){
            singletonCurrentValues=new SingletonCurrentValues();
        }
        return singletonCurrentValues;
    }
}
