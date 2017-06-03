package experiment.LingPipe;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.ChunkerEvaluator;
import com.aliasi.chunk.Chunking;

import com.aliasi.corpus.Corpus;
import com.aliasi.corpus.ObjectHandler;
import com.aliasi.crf.ChainCrf;
import com.aliasi.crf.ChainCrfChunker;

import com.aliasi.util.AbstractExternalizable;
import com.aliasi.util.ScoredObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SimpleEntityChunk {

    public static void main(String[] args) 
        throws ClassNotFoundException, IOException {

        File modelFile = new File("model.crf");
        @SuppressWarnings("unchecked")
        ChainCrfChunker crfChunker
            = (ChainCrfChunker) 
            AbstractExternalizable.readObject(modelFile);
        String []title="Diztronic Matte Back Black Flexible TPU Case &amp; Screen Protector for Motorola Atrix 2 (AT&amp;T) - Retail Packaging".split(" ");
		ChainCrfChunker compiledCrfChunker
				= (ChainCrfChunker)
				AbstractExternalizable.serializeDeserialize(crfChunker);
		ChunkerEvaluator evaluator
				= new ChunkerEvaluator(compiledCrfChunker);
		Corpus<ObjectHandler<Chunking>> corpus
				= new TinyEntityCorpus();
		corpus.visitTest(evaluator);
		System.out.println("\nEvaluation");
		System.out.println(evaluator);
/*        for (int i = 1; i < title.length; ++i) {
            String arg = title[i];
            char[] cs = arg.toCharArray();

            System.out.println("\nFIRST BEST");
            Chunking chunking = crfChunker.chunk(arg);
            System.out.println(chunking);

            int maxNBest = 10;
            System.out.println("\n" + maxNBest + " BEST CONDITIONAL");
            System.out.println("Rank log p(tags|tokens)  Tagging");
            Iterator<ScoredObject<Chunking>> it
                = crfChunker.nBestConditional(cs,0,cs.length,maxNBest);
            for (int rank = 0; rank < maxNBest && it.hasNext(); ++rank) {
                ScoredObject<Chunking> scoredChunking = it.next();
                System.out.println(rank + "    " + scoredChunking.score() + " " + scoredChunking.getObject());
            }

            System.out.println("\nMARGINAL CHUNK PROBABILITIES");
            System.out.println("Rank Chunk Phrase");
            int maxNBestChunks = 10;
            Iterator<Chunk> nBestChunkIt = crfChunker.nBestChunks(cs,0,cs.length,maxNBestChunks);
            for (int n = 0; n < maxNBestChunks && nBestChunkIt.hasNext(); ++n) {
                Chunk chunk = nBestChunkIt.next();
                System.out.println(n + " " + chunk + " " + arg.substring(chunk.start(),chunk.end()));
            }
        }*/

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.print("Enter text followed by new line\n>");
			String evalText = reader.readLine();
			char[] evalTextChars = evalText.toCharArray();
			System.out.println("\nFIRST BEST");
			Chunking chunking = crfChunker.chunk(evalText);
			System.out.println(chunking);

			int maxNBest = 10;
			System.out.println("\n" + maxNBest + " BEST CONDITIONAL");
			System.out.println("Rank log p(tags|tokens)  Tagging");
			Iterator<ScoredObject<Chunking>> it
			= crfChunker.nBestConditional(evalTextChars,0,evalTextChars.length,maxNBest);
			for (int rank = 0; rank < maxNBest && it.hasNext(); ++rank) {
				ScoredObject<Chunking> scoredChunking = it.next();
				System.out.println(rank + "    " + scoredChunking.score() + " " + scoredChunking.getObject().chunkSet());
			}

			System.out.println("\nMARGINAL CHUNK PROBABILITIES");
			System.out.println("Rank Chunk Phrase");
			int maxNBestChunks = 10;
			Iterator<Chunk> nBestChunkIt = crfChunker.nBestChunks(evalTextChars,0,evalTextChars.length,maxNBestChunks);
			for (int n = 0; n < maxNBestChunks && nBestChunkIt.hasNext(); ++n) {
				Chunk chunk = nBestChunkIt.next();
				System.out.println(n + " " + chunk + " " + evalText.substring(chunk.start(),chunk.end()));
			}
		}

    }

}