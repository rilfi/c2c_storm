package experiment;

import com.aliasi.chunk.ChunkFactory;
import com.aliasi.chunk.Chunking;
import com.aliasi.chunk.ChunkingImpl;
import com.aliasi.spell.JaccardDistance;
import com.aliasi.tokenizer.CharacterTokenizerFactory;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by s1 on 5/19/2017.
 */
public class Training2 {
    public static void main(String[] args) throws IOException{


        writeChunking();




    }


    public static void writeChunking( ) {

        String titleFile = "c3TitleSet.txt";
        BufferedWriter writer = null;
        BufferedWriter writer1 = null;
        try {
            writer = new BufferedWriter(new FileWriter("MITrainingData6.txt"));
            writer1 = new BufferedWriter(new FileWriter("MITestData6.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(titleFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        List<String> brandList = null;
        List<String>catList=null;
        try {
            brandList = Files.readAllLines(new File("orderedBrandFinal1.txt").toPath(), Charset.defaultCharset());
            catList = Files.readAllLines(new File("catListSorted1.txt").toPath(), Charset.defaultCharset());

        } catch (IOException e) {
            e.printStackTrace();
        }
        String row;
        int linecount = 0;
        int training = 0;
        int test = 0;
        Chunking[] CHUNKINGS=new Chunking[1000];
        ArrayList<String>trainigList=new ArrayList<>();

        try {
            while ((row = br.readLine()) != null) {
                linecount++;

                row = row.replaceAll("[\\s\\p{Z}]+", " ").trim();
                String rowLower = row.toLowerCase();
                String[] originalTokens = row.split(" ");
                ArrayList<String> tokens = new ArrayList<>(Arrays.asList(rowLower.split(" ")));
                ArrayList<String> otokens = new ArrayList<>(Arrays.asList(rowLower.split(" ")));
                Map<Integer,String>rangeMap=new HashMap<>();
                ReturnObject1 ro=new ReturnObject1();
                for (String brand : brandList) {
                    ro=rangeList(brand,"brand",tokens,otokens,rangeMap);
                    tokens=ro.getwList();
                    rangeMap=ro.getRangeMap();
                }
                for (String brand : catList) {
                    ro=rangeList(brand,"category",tokens,otokens,rangeMap);
                    tokens=ro.getwList();
                    rangeMap=ro.getRangeMap();
                }


                if(rangeMap.size()>0){

                    String tLine=row+"#@#@#";
                    for(Integer start:rangeMap.keySet()){
                        tLine=tLine+String.valueOf(start)+"-"+String.valueOf(rangeMap.get(start))+",";
                    }
                    tLine=tLine.substring(0,tLine.length()-1);

                    training++;
                    writer.write(tLine);
                    writer.newLine();
                    writer.flush();



                }
                else {
                    test++;
                    writer1.write(row);
                    writer1.newLine();
                    writer1.flush();
                }





                System.out.println("line: " + linecount + " test: " + test + " training: " + training +  " size: " + tokens.size());



            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            br.close();
            writer.close();
            writer1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
    public static boolean chardistance(String brand, String token) {
        TokenizerFactory characterTf = CharacterTokenizerFactory.INSTANCE;
        JaccardDistance jaccardCharacter = new JaccardDistance(characterTf);
        double jaccardCharacterDistance = jaccardCharacter.distance(brand, token);
        // System.out.println(jaccardCharacterDistance);
        if (jaccardCharacterDistance < 0.05) {
            return true;
        }
        return false;

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

    public static ReturnObject1 rangeList(String brand,String type, ArrayList<String> wlist,ArrayList<String> olist,Map<Integer,String>rangeMap){

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
        ReturnObject1 ro=new ReturnObject1();
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
class ReturnObject1{
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
