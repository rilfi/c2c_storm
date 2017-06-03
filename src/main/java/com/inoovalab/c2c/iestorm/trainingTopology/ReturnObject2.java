package com.inoovalab.c2c.iestorm.trainingTopology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ReturnObject2{
    public ArrayList<String> getwList() {
        return wList;
    }

    public void setwList(ArrayList<String> wList) {
        this.wList = wList;
    }

    public Map<Integer, String> getRangeMap() {
        return rangeMap;
    }

    public void setRangeMap(Map<Integer, String> rangeMap) {
        this.rangeMap = rangeMap;
    }

    ArrayList<String>wList=new ArrayList<>();
    Map<Integer,String>rangeMap=new HashMap<>();

}