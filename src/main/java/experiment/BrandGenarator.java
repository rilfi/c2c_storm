package experiment;

import com.aliasi.util.Strings;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by a1 on 4/17/2017.
 */
public class BrandGenarator {
    XSSFWorkbook workbook ;
    XSSFSheet spreadsheet ;
    int rowid;

    public static void main(String[] args) throws IOException {
        BrandGenarator brandGenarator = new BrandGenarator();

        String inputPath = "meta_Musical_Instruments.json";
        //BufferedWriter writer;
       // BufferedWriter writer = new BufferedWriter(new FileWriter("music_brands.csv"));

        FileInputStream fileIn = new FileInputStream(inputPath);
        InputStreamReader inputStreamReader = new InputStreamReader(fileIn, Strings.UTF8);
        List<String[]> rows = new ArrayList<String[]>();
        BufferedReader br = new BufferedReader(inputStreamReader);
        String row;
        int t = 0;
        brandGenarator.iniciatePoi();
        while ((row = br.readLine()) != null) {

            //System.out.println(row);
            String brand = "noValue";
            ///String product="noValue";
            String title = "noValue";
            String catagories="noValue";

            t++;
            try {
                JSONObject jsonObject = new JSONObject(row);
               /* if(jsonObject.has("categories")){
                    JSONArray companyList = (JSONArray) jsonObject.get("categories");
                    companyList = companyList.getJSONArray(0);
                    int len = companyList.length();
                    product = companyList.get(len - 1).toString();
                }

                if(jsonObject.has("title")){
                    title = jsonObject.get("title").toString();
                }*/
                if (jsonObject.has("brand")&& jsonObject.has("title")&& jsonObject.has("categories")) {

                    brand = jsonObject.get("brand").toString();
                    title = jsonObject.get("title").toString();
                    catagories=jsonObject.get("categories").toString();
                    if(brand.length()>1 && title.length()>1&&catagories.length()>1) {
                        brandGenarator.writeinExcel(brand, title, catagories);
                    }


                    /*writer.write(brand);
                    writer.newLine();*/
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            /*if (t == 10) {
                br.close();
                writer.close();
                break;
            }*/

        }
        brandGenarator.teminatePoi("MusicInstruments.xlsx");
        System.out.println(t);
        br.close();
       // writer.close();

        // String line=br.readLine();


    }

    public void iniciatePoi() {
        workbook = new XSSFWorkbook();
        spreadsheet = workbook.createSheet(
                " MusicInstruments ");

    }

    public void writeinExcel(String brand, String title,String catagories) {
        XSSFRow row;

        row = spreadsheet.createRow(rowid++);
        int cellid = 0;

        Cell cell = row.createCell(0);
        cell.setCellValue(brand);
        Cell cell1 = row.createCell(1);
        cell1.setCellValue(title);
        Cell cell2 = row.createCell(2);
        cell2.setCellValue(catagories);

    }

    public void teminatePoi(String fileName) throws IOException {
        FileOutputStream out = new FileOutputStream(
                new File(fileName));
        workbook.write(out);
        out.close();
        System.out.println(
                "Writesheet.xlsx written successfully");
    }


}
