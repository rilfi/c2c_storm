package com.inoovalab.c2c.iestorm.trainingTopology;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by a1 on 4/2/2017.
 */
public class tokenizing_rich_Bolt extends BaseRichBolt {
    OutputCollector _collector;
    List<String> brandList = null;
    List<String> catList = null;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        _collector = outputCollector;



    }

    @Override
    public void execute(Tuple tuple) {
        String row =  tuple.getStringByField("row");
        row = row.replaceAll("[\\s\\p{Z}]+", " ").trim();
        String rowLower = row.toLowerCase();
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList(rowLower.split(" ")));
        ArrayList<String> otokens = new ArrayList<>(Arrays.asList(rowLower.split(" ")));
        _collector.emit(new Values(row, tokens, otokens));


    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("row", "tokens", "otokens"));

    }
}
