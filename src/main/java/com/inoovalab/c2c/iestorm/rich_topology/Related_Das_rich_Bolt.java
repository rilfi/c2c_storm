package com.inoovalab.c2c.iestorm.rich_topology;

import com.inoovalab.c2c.iestorm.TweetEvent;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.json.JSONArray;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.File;
import java.util.Map;

/**
 * Created by rilfi on 3/19/2017.
 */
public class Related_Das_rich_Bolt extends BaseRichBolt {
    SslConfigurator sslConfig ;
    SSLContext sslContext ;
    //HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("Basic", "YWRtaW46YWRtaW4=");
    HttpAuthenticationFeature feature ;
    Client client ;
    WebTarget webTarget;
    WebTarget webTargetSc;
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        sslConfig = SslConfigurator.newInstance()
                .trustStoreFile("security"+ File.separator+"client-truststore.jks")
                .trustStorePassword("wso2carbon")
                .keyStoreFile("security"+ File.separator+"wso2carbon.jks")
                .keyPassword("wso2carbon");
        sslContext = sslConfig.createSSLContext();
        //HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("Basic", "YWRtaW46YWRtaW4=");
        feature = HttpAuthenticationFeature.basic("admin", "admin");
        client = ClientBuilder.newBuilder().sslContext(sslContext).build();
        webTarget = client.target("https://localhost:9443").path("analytics/search").register(feature);
        webTargetSc=client.target("https://localhost:9443").path("analytics/search_count").register(feature);

    }
    public  String getRealtedRecordes(String brand,String product,String model, String status){

        String payload = "{\"tableName\":\"INPUTSTREAMTOPERSIST\",\"query\":\"brand:"+brand+"\",\"product:" + product + "\",\"model:" + model + "\",\"status:" + status + "\"}";
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(payload));
        return response.readEntity(String.class);

    }

    @Override
    public void execute(Tuple tuple) {
        TweetEvent tv = (TweetEvent) tuple.getValue(0);
        JSONArray ja = new JSONArray(getRealtedRecordes(tv.getBrand(),tv.getProduct(),tv.getModel(),tv.getStatus()));


    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
