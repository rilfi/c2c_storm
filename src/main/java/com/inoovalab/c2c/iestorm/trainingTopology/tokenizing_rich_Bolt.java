package com.inoovalab.c2c.iestorm.trainingTopology;

import com.aliasi.spell.JaccardDistance;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import com.inoovalab.c2c.iestorm.TweetEvent;
import experiment.ReturnObject1;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by a1 on 4/2/2017.
 */
public class Training_rich_Bolt extends BaseRichBolt {
    OutputCollector _collector;
    List<String> brandList = null;
    List<String>catList=null;
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        _collector = outputCollector;
        try {
            brandList = Files.readAllLines(new File("orderedBrandFinal1.txt").toPath(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            catList = Files.readAllLines(new File("catListSorted1.txt").toPath(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void execute(Tuple tuple) {
        String row=(String) tuple.getValue(0);
        row = row.replaceAll("[\\s\\p{Z}]+", " ").trim();
        String rowLower = row.toLowerCase();
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList(rowLower.split(" ")));
        ArrayList<String> otokens = new ArrayList<>(Arrays.asList(rowLower.split(" ")));
        Map<Integer,String>rangeMap=new HashMap<>();

        _collector.emit( new Values(tv));




    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("emitingMap"));

    }
    public static boolean tokenDistance(String brand, String token) {
        TokenizerFactory indoEuropeanTf = IndoEuropeanTokenizerFactory.INSTANCE;
        JaccardDistance jaccardIndoEuropean = new JaccardDistance(indoEuropeanTf);
        double jaccardIndoEuropeanDistance = jaccardIndoEuropean.distance(brand, token);
        if (jaccardIndoEuropeanDistance < 0.2) {
            return true;
        }
        return false;
    }

    public static ReturnObject2 rangeList(String brand,String type, ArrayList<String> wlist,ArrayList<String> olist,Map<Integer,String>rangeMap){

        String deli="-:@-!:";
        int brandSize=brand.split(" ").length;
        ArrayList<String> ngList=getNgList(brandSize,wlist,olist);
        int ngIndex=0;
        while (ngIndex<ngList.size()-1){
            for(int i=ngIndex;i<ngList.size();i++){
                ngIndex=i;

                String ng=ngList.get(i).split(deli)[0];
                int start=Integer.parseInt(ngList.get(i).split(deli)[1].split("-")[0]);
                int end=Integer.parseInt(ngList.get(i).split(deli)[1].split("-")[1]);
                int lstart=Integer.parseInt(ngList.get(i).split(deli)[1].split("-")[2]);
                int lend=Integer.parseInt(ngList.get(i).split(deli)[1].split("-")[3]);
                if(tokenDistance(brand,ng)){
                    rangeMap.put(lstart,lend+"-"+type);

                    if(brandSize==1){
                        wlist.set(start,"#####-N1");

                    }
                    else if(brandSize==2){
                        wlist.set(start,"#####-No");
                        wlist.set(end,"#####-Nc");

                    }
                    else if(brandSize==3){
                        wlist.set(start,"#####-No");
                        wlist.set(start+1,"#####-Ni");
                        wlist.set(end,"#####-Nc");
                    }



                    ngList=getNgList(brandSize,wlist,olist);
                    break;

                }

            }
        }
        ReturnObject2 ro=new ReturnObject2();
        ro.setRangeMap(rangeMap);
        ro.setwList(wlist);


        return ro;

    }





    static ArrayList<String>getNgList(int brandSize,ArrayList<String>wlist,ArrayList<String>olist){
        ArrayList<String> ngwords = new ArrayList<>();

        int maxsize = wlist.size() - brandSize+1;
        int start=0;
        int end=0;
        String title=String.join(" ",wlist);
        for (int i = 0; i < maxsize; i++) {

            String concatStr = String.join(" ", wlist.subList(i, i + brandSize));
            String oconcatStr = String.join(" ", olist.subList(i, i + brandSize));
            end=start+oconcatStr.length();

            ngwords.add(concatStr+"-:@-!:"+String.valueOf(i)+"-"+String.valueOf(i+brandSize-1)+"-"+String.valueOf(start)+"-"+String.valueOf(end));
            // String ngTags=title.substring(start,end);
            // System.out.println(title.substring(start,end));

            start=start+olist.get(i).length()+1;


        }
        return ngwords;


    }
}
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
