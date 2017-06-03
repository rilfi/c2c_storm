package experiment;

import com.aliasi.util.Strings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a1 on 4/17/2017.
 */
public class CSVGenarator {
    public static void main(String[] args) throws IOException {
        String inputPath = "meta_Electronics.json";
        //BufferedWriter writer;
        BufferedWriter writer = new BufferedWriter(new FileWriter("electronics.csv"));

        FileInputStream fileIn = new FileInputStream(inputPath);
        InputStreamReader inputStreamReader = new InputStreamReader(fileIn, Strings.UTF8);
        List<String[]> rows = new ArrayList<String[]>();
        BufferedReader br = new BufferedReader(inputStreamReader);
        String row;
        int t = 0;
        while ((row = br.readLine()) != null) {

            //System.out.println(row);
            String brand="noValue";
            String product="noValue";
            String title="noValue";

            t++;
            try {
                JSONObject jsonObject = new JSONObject(row);
                if(jsonObject.has("categories")){
                    JSONArray companyList = (JSONArray) jsonObject.get("categories");
                    companyList = companyList.getJSONArray(0);
                    int len = companyList.length();
                    product = companyList.get(len - 1).toString();
                }

                if(jsonObject.has("title")){
                    title = jsonObject.get("title").toString();
                }
                if(jsonObject.has("brand")){
                    brand = jsonObject.get("brand").toString();
                }

                writer.write(brand + ",,,," + product + ",,,," + title);
                writer.newLine();


            } catch (Exception e) {
                e.printStackTrace();
            }

            /*if (t == 10) {
                br.close();
                writer.close();
                break;
            }*/

        }
        System.out.println(t);
        br.close();
        writer.close();

        // String line=br.readLine();


    }
}
