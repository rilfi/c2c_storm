//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package experiment;

import info.debatty.java.stringsimilarity.CharacterSubstitutionInterface;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Damerau;
import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.Levenshtein;
import info.debatty.java.stringsimilarity.LongestCommonSubsequence;
import info.debatty.java.stringsimilarity.NGram;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.OptimalStringAlignment;
import info.debatty.java.stringsimilarity.QGram;
import info.debatty.java.stringsimilarity.SorensenDice;
import info.debatty.java.stringsimilarity.WeightedLevenshtein;

public class StringSmilarilies {
    public StringSmilarilies() {
    }

    public static void main(String[] args) {
        String one="acer gateway";
        String two="acer";
        System.out.println("\nLevenshtein");
        Levenshtein levenshtein = new Levenshtein();
        System.out.println(levenshtein.distance(one, "two"));
        System.out.println("\nJaccard");
        Jaccard j2 = new Jaccard(2);
        System.out.println(j2.similarity(one, two));
        System.out.println("\nJaro-Winkler");
        JaroWinkler jw = new JaroWinkler();
        System.out.println(jw.similarity(one,two));
        System.out.println("\nCosine");
        Cosine cos = new Cosine(3);
        System.out.println(cos.similarity(one,two));
        cos = new Cosine(2);
        System.out.println(cos.similarity(one, two));
        System.out.println("\nDamerau");
        Damerau damerau = new Damerau();
        System.out.println(damerau.distance(one,two));

        System.out.println("\nOptimal String Alignment");
        OptimalStringAlignment osa = new OptimalStringAlignment();
        System.out.println(osa.distance(one,two));
        System.out.println("\nLongest Common Subsequence");
        LongestCommonSubsequence lcs = new LongestCommonSubsequence();
        System.out.println(lcs.distance(one, two));
        System.out.println("\nNGram");
        NGram twogram = new NGram(2);
        System.out.println(twogram.distance(one, two));
        NGram ngram = new NGram(4);
        System.out.println(ngram.distance(one, two));
        System.out.println("\nNormalized Levenshtein");
        NormalizedLevenshtein l = new NormalizedLevenshtein();
        System.out.println(l.distance(one, two));
        System.out.println("\nQGram");
        QGram dig = new QGram(2);
        System.out.println(dig.distance(one, two));

        System.out.println("\nSorensen-Dice");
        SorensenDice sd = new SorensenDice(2);
        System.out.println(sd.similarity(one, two));
        System.out.println("\nWeighted Levenshtein");
        WeightedLevenshtein wl = new WeightedLevenshtein(new CharacterSubstitutionInterface() {
            public double cost(char c1, char c2) {
                return c1 == 116 && c2 == 114?0.5D:1.0D;
            }
        });
        System.out.println(wl.distance(one, two));
        System.out.println("\nK-Shingling");

        Cosine cosine = new Cosine(4);
        System.out.println(cosine.getProfile(one));
        System.out.println(cosine.getProfile(two));
        cosine = new Cosine(2);
        System.out.println(cosine.getProfile(one));
    }
}
