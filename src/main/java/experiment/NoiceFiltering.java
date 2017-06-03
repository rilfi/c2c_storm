package experiment;

import com.aliasi.spell.JaccardDistance;
import com.aliasi.tokenizer.CharacterTokenizerFactory;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.util.Strings;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;


/**
 * Created by a1 on 4/17/2017.
 */
public class NoiceFiltering {


    public static void main(String[] args) throws IOException {
        NoiceFiltering noiceFiltering = new NoiceFiltering();
/*
        //System.out.println( noiceFiltering.UniqAtrib("meta_Digital_Music.json"));
        noiceFiltering.iniciatePoi("digitalMusic");
        FileInputStream fileIn = new FileInputStream("meta_Digital_Music.json");
        InputStreamReader inputStreamReader = new InputStreamReader(fileIn, Strings.UTF8);
        List<String[]> rows = new ArrayList<String[]>();
        BufferedReader br = new BufferedReader(inputStreamReader);
        String row;
        while ((row = br.readLine()) != null) {
            String fields=noiceFiltering.jsonToFields(row);
            String[]fieldArray=fields.split("@#@");
            noiceFiltering.writeinExcel(fieldArray[0],fieldArray[1],fieldArray[2]);

        }
        noiceFiltering.teminatePoi("digitalFields.xlsx");*/
//noiceFiltering.brandNormaize();
        noiceFiltering.sortBrands();
        // System.out.println(noiceFiltering.chardistance("samsung","samsumg"));


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

    public String jsonToFields(String row) {
        JSONObject jsonObject = new JSONObject(row);

        String brand = "noValue";
        ///String product="noValue";
        String title = "noValue";
        String catagories = "noValue";
        if (jsonObject.has("brand") && jsonObject.has("title") && jsonObject.has("categories")) {

            brand = jsonObject.get("brand").toString();
            title = jsonObject.get("title").toString();
            catagories = jsonObject.get("categories").toString();

        }
        return brand + "@#@" + title + "@#@" + catagories;
    }

    public void writeOnlyTitle() throws IOException {
        String[] fileNames = {"meta_Musical_Instruments.json", "meta_Cell_Phones_and_Accessories.json", "meta_Electronics.json"};
        BufferedWriter writer = new BufferedWriter(new FileWriter("c3Title.txt"));
        for (String filename : fileNames) {

            FileInputStream fileIn = new FileInputStream(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fileIn, Strings.UTF8);
            BufferedReader br = new BufferedReader(inputStreamReader);

            String row;
            while ((row = br.readLine()) != null) {
                try {
                    JSONObject jsonObject = new JSONObject(row);
                    if (jsonObject.has("title")) {
                        writer.write(jsonObject.get("title").toString());
                        writer.newLine();

                    }
                } catch (JSONException e) {
                    System.out.println("json error");
                }

            }
            br.close();
        }

        writer.close();
    }

/*    public void iniciatePoi(String sheetName) {
        workbook = new XSSFWorkbook();
        spreadsheet = workbook.createSheet(sheetName);

    }*/

/*    public void writeinExcel(String brand, String title, String catagories) {
        String allField = brand + title + catagories;
        if (allField.contains("noValue")) {
            return;
        }
        XSSFRow row;

        row = spreadsheet.createRow(rowid++);
        int cellid = 0;

        Cell cell = row.createCell(0);
        cell.setCellValue(brand);
        Cell cell1 = row.createCell(1);
        cell1.setCellValue(title);
        Cell cell2 = row.createCell(2);
        cell2.setCellValue(catagories);

    }*/

  /*  public void teminatePoi(String fileName) throws IOException {
        FileOutputStream out = new FileOutputStream(
                new File(fileName));
        workbook.write(out);
        out.close();
        System.out.println(
                " written successfully");
    }*/

    public void removedublicate() throws IOException {
       BufferedWriter writer = new BufferedWriter(new FileWriter("c3TitleSet.txt"));
        FileInputStream fileIn = new FileInputStream("c3Title.txt");
        InputStreamReader inputStreamReader = new InputStreamReader(fileIn, Strings.UTF8);
        BufferedReader br = new BufferedReader(inputStreamReader);
        String row;
        Set<String> set1 = new HashSet<>();
        while ((row = br.readLine()) != null) {

            set1.add(row);


        }
        br.close();
        /*Set<String>set2=set1;
        Set<String>set3=new HashSet<>();
        point:
        for(String ti:set1){
            ArrayList<String>temp=new ArrayList<>();
            for(String tit:set2){
                if(tokenDistance(ti,tit)){
                    temp.add(ti);
                }


            }
            for(String t1:temp){
                set1.remove(t1);
                set2.remove(t1);
            }
            //
            writer.write(temp.get(0));
            writer.newLine();



            break point;


        }*/
        for(String st:set1){
            if(st.split(" ").length<4)
                continue;

            writer.write(st);
            writer.newLine();
            writer.flush();
        }
        writer.close();


    }


    public Set<String> UniqAtrib(String fileName) throws IOException {
        FileInputStream fileIn = new FileInputStream(fileName);
        Set<String> keySet = new HashSet<>();
        InputStreamReader inputStreamReader = new InputStreamReader(fileIn, Strings.UTF8);
        List<String[]> rows = new ArrayList<String[]>();
        BufferedReader br = new BufferedReader(inputStreamReader);
        String row;
        while ((row = br.readLine()) != null) {
            JSONObject jsonObject = new JSONObject(row);
            for (Object key : jsonObject.keySet()) {
                keySet.add(key.toString());

            }
        }
        return keySet;
    }

    public void brandNormaize() throws IOException {
        FileInputStream fileIn = new FileInputStream("multiwordBrandMI.txt");
        InputStreamReader inputStreamReader = new InputStreamReader(fileIn, Strings.UTF8);
        BufferedReader br = new BufferedReader(inputStreamReader);
        BufferedWriter writer = new BufferedWriter(new FileWriter("MusicBands2.txt"));
        String row;
        while ((row = br.readLine()) != null) {
            row = row.trim();

            if (row.length() < 4 || row.split(" ").length > 3) {
                continue;
            }
            writer.write(row);
            writer.newLine();

        }
        writer.close();
        br.close();
    }

    public void sortBrands() throws IOException {
        List<String> list = Files.readAllLines(new File("catListUnsorted1.txt").toPath(), Charset.defaultCharset());
        int maxLenth = 0;
        BufferedWriter writer = new BufferedWriter(new FileWriter("catListSorted1.txt"));
        for (String line : list) {
            if (line.length() > maxLenth)
                maxLenth = line.length();

        }
        List<String> finalList = new ArrayList<>();


        for (int i = maxLenth; i > 3; i--) {
            SortedSet set = new TreeSet();
            for (String line : list) {
                if (line.length() == i) {
                    set.add(line);
                }
            }
            finalList.addAll(set);


        }

        for (String line : finalList) {
            writer.write(line);
            writer.newLine();
        }
        writer.close();


    }



    public void modelExtract() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();


        String[] productList = {"meta_Electronics.json", "meta_Cell_Phones_and_Accessories.json", "meta_Musical_Instruments.json"};
        Set<String> totalBrands = new HashSet<>();

        for (String product : productList) {
            XSSFSheet spreadsheet = workbook.createSheet(product.replace(".json", ""));

            FileInputStream fileIn = new FileInputStream(product);
            InputStreamReader inputStreamReader = new InputStreamReader(fileIn, Strings.UTF8);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String row;
            int rowid = 0;
            Set<String> brands = new HashSet<>();
            while ((row = br.readLine()) != null) {

                try {
                    JSONObject jsonObject = new JSONObject(row);
                    if (jsonObject.has("brand")) {
                        String brand = jsonObject.get("brand").toString();
                        brand = brand.replaceAll("[\\s\\p{Z}]+", " ").trim();
                        if (brand.length() < 2)
                            continue;
                        boolean isAlphNu = true;
                        for (String part : brand.split(" ")) {
                            if (!isAlphanumeric(part)) {
                                isAlphNu = false;
                                break;
                            }

                        }
                        if (!isAlphNu)
                            continue;
                        if (brand.split(" ").length > 4)
                            continue;

                        brands.add(brand);
                        totalBrands.add(brand);


                    }
                } catch (JSONException e) {
                    System.out.println("json error");
                }

            }
            for (String brnd : brands) {
                XSSFRow xlrow;

                xlrow = spreadsheet.createRow(rowid++);
                Cell cell = xlrow.createCell(0);
                cell.setCellValue(brnd);
            }
            br.close();


        }
        XSSFSheet spreadsheet = workbook.createSheet("allBrands");
        int rowNo = 0;
        for (String brnd : totalBrands) {
            XSSFRow xlrow;

            xlrow = spreadsheet.createRow(rowNo++);
            Cell cell = xlrow.createCell(0);
            cell.setCellValue(brnd);
        }

        FileOutputStream out = new FileOutputStream(
                new File("brands.xlsx"));
        workbook.write(out);
        out.close();
    }

    public void catExtract() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();


        String[] productList = {"meta_Electronics.json", "meta_Cell_Phones_and_Accessories.json", "meta_Musical_Instruments.json"};
        Set<String> totalBrands = new HashSet<>();

        for (String product : productList) {
            XSSFSheet spreadsheet = workbook.createSheet(product.replace(".json", ""));

            FileInputStream fileIn = new FileInputStream(product);
            InputStreamReader inputStreamReader = new InputStreamReader(fileIn, Strings.UTF8);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String row;
            int rowid = 0;
            Set<String> brands = new HashSet<>();
            while ((row = br.readLine()) != null) {

                try {
                    JSONObject jsonObject = new JSONObject(row);
                    if (jsonObject.has("categories")) {
                        String brand = jsonObject.get("categories").toString();
                        JSONArray catList = new JSONArray(brand);
                         catList=  catList.getJSONArray(0);
                        int o=10;
                        for (int k = 0; k < catList.length(); k++){
                            brand=catList.getString(k);
                            brand = brand.replaceAll("[\\s\\p{Z}]+", " ").trim();
                        if (brand.length() < 2)
                            continue;
                        boolean isAlphNu = true;
                        for (String part : brand.split(" ")) {
                            if (!isAlphanumeric(part)) {
                                isAlphNu = false;
                                break;
                            }

                        }
                        if (!isAlphNu)
                            continue;
                        if (brand.split(" ").length > 4)
                            continue;

                        brands.add(brand);
                        totalBrands.add(brand);


                    }
                }
                } catch (JSONException e) {
                    System.out.println("json error");
                }

            }
            for (String brnd : brands) {
                XSSFRow xlrow;

                xlrow = spreadsheet.createRow(rowid++);
                Cell cell = xlrow.createCell(0);
                cell.setCellValue(brnd);
            }
            br.close();


        }
        XSSFSheet spreadsheet = workbook.createSheet("allcat");
        int rowNo = 0;
        for (String brnd : totalBrands) {
            XSSFRow xlrow;

            xlrow = spreadsheet.createRow(rowNo++);
            Cell cell = xlrow.createCell(0);
            cell.setCellValue(brnd);
        }

        FileOutputStream out = new FileOutputStream(
                new File("categories.xlsx"));
        workbook.write(out);
        out.close();
    }

    public void onlyOneWord() throws IOException {
        FileInputStream fileIn = new FileInputStream("allbrands.txt");
        InputStreamReader inputStreamReader = new InputStreamReader(fileIn, Strings.UTF8);
        BufferedReader br = new BufferedReader(inputStreamReader);
        BufferedWriter writer = new BufferedWriter(new FileWriter("oneWordBrands.txt"));
        BufferedWriter writer1 = new BufferedWriter(new FileWriter("multiWordBrands.txt"));

        String row;
        Set<String> oneset = new HashSet<>();
        Set<String> multiword = new HashSet<>();
        while ((row = br.readLine()) != null) {
            if (row.split(" ").length > 1) {

                if (multiword.contains(row.toLowerCase())) {
                    continue;
                }
                multiword.add(row);
            }
          /*if(oneset.contains(row.toLowerCase())){
              continue;
          }
          oneset.add(row.toLowerCase());*/


        }
     /* for(String brnd:oneset) {
          writer.write(brnd);
          writer.newLine();
      }*/
        for (String brnd : multiword) {
            writer1.write(brnd);
            writer1.newLine();
        }
        br.close();
        //writer.close();
        writer1.close();
    }

    public void lenthfilterWord() throws IOException {
        FileInputStream fileIn = new FileInputStream("multiWordBrand.txt");
        InputStreamReader inputStreamReader = new InputStreamReader(fileIn, Strings.UTF8);
        BufferedReader br = new BufferedReader(inputStreamReader);
        BufferedWriter writer = new BufferedWriter(new FileWriter("twoWordBrands.txt"));
        BufferedWriter writer1 = new BufferedWriter(new FileWriter("threeWordBrands.txt"));
        BufferedWriter writer2 = new BufferedWriter(new FileWriter("fourWordBrands.txt"));

        String row;
        Set<String> twoset = new HashSet<>();
        Set<String> threeword = new HashSet<>();
        Set<String> fourword = new HashSet<>();
        while ((row = br.readLine()) != null) {
            if (row.split(" ").length == 2) {

                if (twoset.contains(row.toLowerCase())) {
                    continue;
                }
                twoset.add(row);
            }
            if (row.split(" ").length == 3) {

                if (threeword.contains(row.toLowerCase())) {
                    continue;
                }
                threeword.add(row);
            }
            if (row.split(" ").length == 4) {

                if (fourword.contains(row.toLowerCase())) {
                    continue;
                }
                fourword.add(row);
            }

          /*if(oneset.contains(row.toLowerCase())){
              continue;
          }
          oneset.add(row.toLowerCase());*/


        }
     /* for(String brnd:oneset) {
          writer.write(brnd);
          writer.newLine();
      }*/

        for (String brnd : twoset) {
            writer.write(brnd);
            writer.newLine();
        }
        for (String brnd : threeword) {
            writer1.write(brnd);
            writer1.newLine();
        }
        for (String brnd : fourword) {
            writer2.write(brnd);
            writer2.newLine();
        }
        br.close();
        writer.close();
        writer1.close();
        writer2.close();
    }

    public boolean isAlphanumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isDigit(c) && !Character.isLetter(c))
                return false;
        }

        return true;
    }

    public boolean chardistance(String brand, String token) {
        TokenizerFactory characterTf = CharacterTokenizerFactory.INSTANCE;
        JaccardDistance jaccardCharacter = new JaccardDistance(characterTf);
        double jaccardCharacterDistance = jaccardCharacter.distance(brand, token);
        System.out.println(jaccardCharacterDistance);
        if (jaccardCharacterDistance < 0.2) {
            return true;
        }
        return false;

    }
}

