package com.inoovalab.c2c.iestorm.trainingTopology;


import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

public class TrainingTopologyRunner_rich {
  public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {

    TopologyBuilder builder = new TopologyBuilder();

    builder.setSpout("File_rich_Spout", new File_rich_Spout(), 1);

    builder.setBolt("tokenizing_rich_Bolt", new tokenizing_rich_Bolt(), 1).shuffleGrouping("File_rich_Spout");
    builder.setBolt("Brand_rich_Bolt", new Brand_rich_Bolt(), 10).shuffleGrouping("tokenizing_rich_Bolt");
    builder.setBolt("catogory_rich_Bolt", new catogory_rich_Bolt(), 10).shuffleGrouping("Brand_rich_Bolt");
    builder.setBolt("file_rich_Bolt", new file_rich_Bolt(), 1).shuffleGrouping("catogory_rich_Bolt");

    Config config = new Config();
    config.setDebug(true);
    //config.registerSerialization(TweetEvent.class);
    config.put("fileName", "c3TitleSet.txt");
    config.put("brand", "orderedBrandFinal1.txt");
    config.put("catagory","catListSorted1.txt");
    config.put("trainingFile","MITrainingData7.txt");
    config.put("testFile","MITestData7.txt");
    //StormSubmitter.submitTopologyWithProgressBar("training",config,builder.createTopology());
    StormTopology topology = builder.createTopology();
    StormSubmitter.submitTopology("training-rich_topology", config, topology);


/*    LocalCluster localCluster = new LocalCluster();
    localCluster.submitTopology("training-rich_topology", config, builder.createTopology());

    Utils.sleep(10000);
    localCluster.killTopology("training-rich_topology");
    localCluster.shutdown();*/
  }
}