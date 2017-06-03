package experiment;

import com.aliasi.spell.JaccardDistance;
import com.aliasi.tokenizer.CharacterTokenizerFactory;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by s1 on 5/4/2017.
 */
public class Training {
    public static void main(String[] args) throws IOException {
        String titleFile = "c3TitleSet3.txt";
        FileInputStream fis = new FileInputStream(titleFile);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        List<String> brandList = Files.readAllLines(new File("orderedBrandFinal3.txt").toPath(), Charset.defaultCharset());
        String row;
        BufferedWriter writer = new BufferedWriter(new FileWriter("MITrainingData3.txt"));
        BufferedWriter writer1 = new BufferedWriter(new FileWriter("MITestData3.txt"));
        String[] tagOder = {"B1", "BoBc", "BoBiBc"};
        int linecount = 0;
        int training = 0;
        int test = 0;
        nextLine:
        while ((row = br.readLine()) != null) {
            linecount++;


            boolean isHaveFullTag = false;
            ArrayList<int[]> regBrandBoundries = new ArrayList<>();
            row = row.replaceAll("[\\s\\p{Z}]+", " ").trim();
            String rowLower = row.toLowerCase();
            String[] originalTokens = row.split(" ");

            ArrayList<String> tokens = new ArrayList<>(Arrays.asList(rowLower.split(" ")));
            Map<Integer, String> tokenMap = new HashMap<>();
            int lastcont = 0;
            int max1 = 0;

            System.out.println("line: " + linecount + " test: " + test + " training: " + training + " lattCount: " + lastcont + " size: " + tokens.size());
            //while (lastcont==1) {

            boolean fullBarandDone = false;
            boolean ispartialDone = false;

            // String[] tokens1 = {"kkkkk"};

            // while (!ispartialDone) {
            //tokens1 = tokens;
            // if (!fullBarandDone) {
            int maxBrand = 0;
            int brandi = 0;
            int tokeni = 0;
            thatWhile:
            while (brandi < brandList.size() || tokeni < tokens.size()) {

                int bb = 0;
                for (String brand : brandList) {


                    bb++;
                    brandi = bb;


                    String capturedBrand = "";
                    boolean isBrandIntercept = false;
                    String[] brandToken = brand.split(" ");


                    if (!isBanandContain(brand,rowLower)) {
                        tokeni = tokens.size();
                        continue;
                    } else {

                        if (linecount == 204)
                            System.out.println(rowLower);

                        maxBrand++;
                        if (maxBrand > max1) {
                            max1 = maxBrand;
                        }

                        int tokenIndex = 0;
                        int brandIndex = 0;
                        int tagIndex = -1;
                        boolean iscontnue = true;
                        Map<Integer, String> labelMap = new HashMap<>();
                        nextBrand:
                        while (true) {


                            for (String bran : brandToken) {

                                for (int i = tokenIndex; i < tokens.size(); i++) {
                                    tokenIndex = i + 1;
                                    tokeni = i;
                                    if (chardistance(bran, tokens.get(i))) {
                                        if (tagIndex == -1) {
                                            tagIndex = i;
                                        } else if (i != tagIndex + 1) {
                                            iscontnue = false;
                                            continue nextBrand;


                                        } else if (i == tagIndex + 1) {
                                            tagIndex = i;
                                        }
                                        // if (bran.equals(tokens[i])) {
                                        capturedBrand += " " + bran;

                                        if (brandToken.length == 1) {
                                            labelMap.put(brandIndex, i + "-B1");


                                        } else if (brandIndex == 0) {
                                            labelMap.put(brandIndex, i + "-Bo");
                                        } else if (brandIndex > 0 && brandIndex < brandToken.length - 1) {
                                            labelMap.put(brandIndex, i + "-Bi");
                                        } else {
                                            labelMap.put(brandIndex, i + "-Bc");
                                        }

                                        brandIndex++;
                                        break;
                                    }


                                }


                            /*if(!labelMap.containsKey(bran)){
                                break;
                            }*/


                            }
                            break ;
                        }


                        String pt = "";
                        for (int h = 0; h < labelMap.size(); h++) {
                            pt += labelMap.get(h).split("-")[1];
                        }
                                /*System.out.println(tagOder.toString());
                                System.out.println(brandToken);
                                System.out.printf(String.valueOf(brandToken.length - 1));*/
                        if (tagOder[brandToken.length - 1].equals(pt)) {
                            // System.out.println(brand+" - "+String.join(" ",tokens));

                            int min = Integer.parseInt(labelMap.get(0).split("-")[0]);
                            int max = Integer.parseInt(labelMap.get(labelMap.size() - 1).split("-")[0]);
                            int range[] = {min, max};
                                    /*if((max-min)+1!=labelMap.size())
                                        continue;*/
                            if (regBrandBoundries.size() > 0) {
                                for (int[] rg : regBrandBoundries) {
                                    if ((min >= rg[0] && min <= rg[1]) || (max >= rg[0] && max <= rg[1])) {
                                        isBrandIntercept = true;
                                        break;

                                    }
                                }
                            }

                            if (isBrandIntercept)
                                continue;

                            regBrandBoundries.add(range);


                            isHaveFullTag = true;

                            String capBand = "";
                            for (int t = 0; t < brandToken.length; t++) {
                                int tokenOrder = Integer.parseInt(labelMap.get(t).split("-")[0]);
                                String tag = labelMap.get(t).split("-")[1];
                                capBand += " " + tag;
                                tokenMap.put(tokenOrder, tag);
                            }
                            capBand = capturedBrand.trim();
                            if (!tokenDistance(brand, capBand))
                                continue;


                            String[] remainingTokence = new String[originalTokens.length - brandToken.length];

                            for (int j = 0; j < originalTokens.length; j++) {
                                if (tokenMap.containsKey(j)) {
                                    tokens.set(j, "#######");

                                }
                            }
                            rowLower = String.join(" ", tokens);


                            //break;

                        } /*else {
                                    if (labelMap.size() != 0)
                                        System.out.println(labelMap);
                                }*/


                    }


                    continue thatWhile;
                }


                fullBarandDone = true;
            }

            //}

            //}
            /*else {

                        for (String brand : brandList) {
                            String capturedBrand = "";
                            boolean isBrandIntercept = false;
                            String[] brandToken = brand.split(" ");
                            if (!rowLower.contains(brandToken[0])) {
                                continue;
                            } else {


                                int tokenIndex = 0;
                                int brandIndex = 0;
                                Map<Integer, String> labelMap = new HashMap<>();
                                for (String bran : brandToken) {
                                    for (int i = tokenIndex; i < tokens.length; i++) {
                                        tokenIndex = i + 1;

                                        if(chardistance(bran,tokens[i])){
                                      //  if (bran.equals(tokens[i])) {
                                            capturedBrand += " " + bran;

                                            if (brandToken.length == 1) {
                                                labelMap.put(brandIndex, i + "-B1");


                                            } else if (brandIndex == 0) {
                                                labelMap.put(brandIndex, i + "-Bo");
                                            } else if (brandIndex > 0 && brandIndex < brandToken.length - 1) {
                                                labelMap.put(brandIndex, i + "-Bi");
                                            } else {
                                                labelMap.put(brandIndex, i + "-Bc");
                                            }

                                            brandIndex++;
                                            break;
                                        }


                                    }

                            *//*if(!labelMap.containsKey(bran)){
                                break;
                            }*//*


                                }



                                //if (tagOder[brandToken.length - 1].equals(pt)) {
                                if(labelMap.size()==0)
                                    continue;
                                int min = Integer.parseInt(labelMap.get(0).split("-")[0]);
                                int max = Integer.parseInt(labelMap.get(labelMap.size() - 1).split("-")[0]);
                                if((max-min)+1!=labelMap.size())
                                    continue;
                                int range[] = {min, max};
                                if (regBrandBoundries.size() > 0) {
                                    for (int[] rg : regBrandBoundries) {
                                        if ((min >= rg[0] && min <= rg[1]) || (max >= rg[0] && max <= rg[1])) {
                                            isBrandIntercept = true;
                                            break;

                                        }
                                    }
                                }

                                if (isBrandIntercept)
                                    continue;

                                regBrandBoundries.add(range);


                                isHaveFullTag = true;

                                String capBand="";
                                for (int t = 0; t < labelMap.size(); t++) {
                                    int tokenOrder = Integer.parseInt(labelMap.get(t).split("-")[0]);
                                    String tag = labelMap.get(t).split("-")[1];
                                    capBand+=" "+tag;
                                    tokenMap.put(tokenOrder, tag);
                                }
                                capBand=capturedBrand.trim();
                                if(!tokenDistance(brand,capBand))
                                    continue;


                               // String[] remainingTokence = new String[originalTokens.length - brandToken.length];

                                for (int j = 0; j < originalTokens.length; j++) {
                                    if (tokenMap.containsKey(j)) {
                                        tokens[j] = "#######";

                                    }
                                }


                                //break;

                       *//* } else {
                            if (labelMap.size() != 0)
                                System.out.println(labelMap);
                        }*//*


                            }


                        }
                        ispartialDone=true;
                    }*/
            //}


            if (isHaveFullTag == false) {
                test++;
                for (String tok : originalTokens) {
                    writer1.write(tok + " NA");
                    writer1.newLine();
                }
                writer1.write(". NA");
                writer1.newLine();
            } else {
                training++;
                for (int j = 0; j < originalTokens.length; j++) {
                    if (tokenMap.containsKey(j)) {
                        writer.write(originalTokens[j] + " " + tokenMap.get(j));

                    } else {
                        writer.write(originalTokens[j] + " NA");


                    }
                    writer.newLine();

                }

                writer.write(". NA");
                writer.newLine();
            }
            writer.flush();

        }

        writer.close();
        writer1.close();
        br.close();


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
        if (jaccardIndoEuropeanDistance < 0.1) {
            return true;
        }
        return false;
    }

    public static boolean isBanandContain(String brand, String title) {
        ArrayList<String> words = new ArrayList<>(Arrays.asList(title.split(" ")));
        ArrayList<String> ngwords = new ArrayList<>();


        int size = brand.split(" ").length;
        int maxsize = words.size() - size;
        for (int i = 0; i < maxsize; i++) {
            String concatStr = String.join(" ", words.subList(i, i + size));

            ngwords.add(concatStr);

        }
        for (String ng : ngwords) {
            if (tokenDistance(brand, ng)) {
                return true;
            }
        }
        return false;
    }
    public static ArrayList<String> lableList(String brand,ArrayList<String> wlist){
        int brandSize=brand.split(" ").length;
        ArrayList<String> ngList=getNgList(brandSize,wlist);
        int ngIndex=0;
        while (ngIndex<ngList.size()){
            for(int i=0;i<ngList.size();i++){
                String ng=ngList.get(i).split(" ")[0];
                int start=Integer.parseInt(ngList.get(i).split(" ")[1].split("-")[0]);
                int end=Integer.parseInt(ngList.get(i).split(" ")[1].split("-")[1]);
                if(tokenDistance(brand,ng)){
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



                    ngList=getNgList(brandSize,wlist);
                    break;

                }

            }
        }
        return wlist;

    }
    public static ArrayList<String>getNgList(int brandSize,ArrayList<String>wlist){
        ArrayList<String> ngwords = new ArrayList<>();

        int maxsize = wlist.size() - brandSize;
        for (int i = 0; i < maxsize; i++) {
            String concatStr = String.join(" ", wlist.subList(i, i + brandSize));

            ngwords.add(concatStr+"*&*&"+i+"-"+(i+brandSize));


        }
        return ngwords;


    }





}
