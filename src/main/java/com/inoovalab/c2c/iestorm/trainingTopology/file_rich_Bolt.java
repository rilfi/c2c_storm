package com.inoovalab.c2c.iestorm.trainingTopology;

import com.aliasi.spell.JaccardDistance;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by a1 on 4/2/2017.
 */
public class file_rich_Bolt extends BaseRichBolt {
    OutputCollector _collector;
    List<String> catList = null;
    private ArrayList<String> tokens;
    private ArrayList<String> otokens;
    private String row;
    BufferedWriter writer = null;
    BufferedWriter writer1 = null;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        _collector = outputCollector;
/*        String trainingfile= (String) map.get("trainingFile");
        String testfile=(String)map.get("testFile");*/
        try {
            writer = new BufferedWriter(new FileWriter("/root/c2c/c2c_storm/MITrainingData7.txt"));
            writer1 = new BufferedWriter(new FileWriter("/root/c2c/c2c_storm/MITestData7.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        
        
        


    }

    @Override
    public void execute(Tuple tuple) {
        Map<Integer,String>rangeMap= (Map<Integer, String>) tuple.getValueByField("rangeMap");
        row=tuple.getStringByField("row");

        if(rangeMap.size()>0){

            String tLine=row+"#@#@#";
            for(Integer start:rangeMap.keySet()){
                tLine=tLine+String.valueOf(start)+"-"+String.valueOf(rangeMap.get(start))+",";
            }
            tLine=tLine.substring(0,tLine.length()-1);

            try {
                writer.write(tLine);
                writer.newLine();
                writer.flush();
                _collector.ack(tuple);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {

            try {
                writer1.write(row);
                writer1.newLine();
                writer1.flush();
                _collector.ack(tuple);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        






    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //outputFieldsDeclarer.declare(new Fields("row","rangeMap"));

    }
    @Override
    public void cleanup() {
        try {
            writer.close();
            writer1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

