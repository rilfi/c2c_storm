package experiment;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunking;
import com.aliasi.dict.ApproxDictionaryChunker;
import com.aliasi.dict.DictionaryEntry;
import com.aliasi.dict.TrieDictionary;
import com.aliasi.spell.FixedWeightEditDistance;
import com.aliasi.spell.WeightedEditDistance;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.util.Strings;

import java.io.*;
import java.util.Set;

/**
 * Created by s1 on 4/26/2017.
 */
public class BrandRecognizer {
    public static void main(String[] args) throws IOException {
        String inputPath="notLongBrands.csv";
        String titlePath="electronics.csv";
        FileInputStream fileIn = new FileInputStream(inputPath);
        InputStreamReader inputStreamReader = new InputStreamReader(fileIn, Strings.UTF8);
        BufferedReader br = new BufferedReader(inputStreamReader);
        FileInputStream fileIn1 = new FileInputStream(titlePath);
        InputStreamReader inputStreamReader1 = new InputStreamReader(fileIn1, Strings.UTF8);
        BufferedReader br1 = new BufferedReader(inputStreamReader1);
        String row;
        String row1;
        int t = 0;
        TrieDictionary<String> dict = new TrieDictionary<String>();
        while ((row = br.readLine()) != null) {
            if(row.length()<5){
                continue;
            }
            dict.addEntry(new DictionaryEntry<String>(row,"brand"));



        }
        System.out.println("dic complete");
        TokenizerFactory tokenizerFactory
                = IndoEuropeanTokenizerFactory.INSTANCE;
        WeightedEditDistance editDistance
                = new FixedWeightEditDistance(0,-1,-1,-1,Double.NaN);

        double maxDistance = 0.0;

        ApproxDictionaryChunker chunker
                = new ApproxDictionaryChunker(dict,tokenizerFactory,
                editDistance,maxDistance);
        while ((row1 = br1.readLine()) != null) {
            t++;
            if(t>10){
                break;
            }
            String text=row1.split(",,,,")[2];
            //for (String text : args) {

            System.out.println("\n\n " + text + "\n");
            Chunking chunking = chunker.chunk(text);
            CharSequence cs = chunking.charSequence();
            Set<Chunk> chunkSet = chunking.chunkSet();

            System.out.printf("%15s  %15s   %8s\n",
                    "Matched Phrase",
                    "Dict Entry",
                    "Distance");
            for (Chunk chunk : chunkSet) {
                int start = chunk.start();
                int end = chunk.end();
                CharSequence str = cs.subSequence(start,end);
                double distance = chunk.score();
                String match = chunk.type();
                System.out.printf("%15s  %15s   %8.1f\n",
                        str, match, distance);
            }




        }




    }
}
