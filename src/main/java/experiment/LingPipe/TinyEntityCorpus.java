package experiment.LingPipe;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.ChunkFactory;
import com.aliasi.chunk.Chunking;
import com.aliasi.chunk.ChunkingImpl;

import com.aliasi.corpus.Corpus;
import com.aliasi.corpus.ObjectHandler;
import experiment.Training1;

import java.io.*;

public class TinyEntityCorpus extends Corpus<ObjectHandler<Chunking>> {

    static final Chunking[] CHUNKINGS
            = getChunking();

    public static Chunking[] getChunking() {
        String titleFile = "MITestData6.txt";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(titleFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        Chunking[] CHUNKINGS = new Chunking[100];
        String row;
        int linecount = 0;
        String deli = "#@*#";

        try {
            while ((row = br.readLine()) != null) {

                String title = row.split("#@#@#")[0]+".";
                String rangeStr=row.split("#@#@#")[1];
                ChunkingImpl chunking = new ChunkingImpl(title);

                if(!rangeStr.contains(",")){

                    int start = Integer.parseInt(rangeStr.split("-")[0]);
                    int end = Integer.parseInt(rangeStr.split("-")[1]);
                    chunking.add(ChunkFactory.createChunk(start, end, "BND"));

                }
                else {
                    String[] ranges = rangeStr.split(",");
                    for (String range : ranges) {
                        int start = Integer.parseInt(range.split("-")[0]);
                        int end = Integer.parseInt(range.split("-")[1]);

                        chunking.add(ChunkFactory.createChunk(start, end, "BND"));

                    }

                }



                CHUNKINGS[linecount] = chunking;
                System.out.println(linecount);
                linecount++;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CHUNKINGS;

    }

    public void visitTrain(ObjectHandler<Chunking> handler) {
        for (Chunking chunking : CHUNKINGS)
            handler.handle(chunking);
    }

    public void visitTest(ObjectHandler<Chunking> handler) {
        for (Chunking chunking : CHUNKINGS)
            handler.handle(chunking);

        /* no op */
    }

}