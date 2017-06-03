package experiment;

import info.debatty.java.stringsimilarity.*;


public class MyApp {
    
    public static void main (String[] args) {

    }
    public static void normalizedLevenshtein(){
        NormalizedLevenshtein l = new NormalizedLevenshtein();
        System.out.println(l.distance("My string", "My $tring"));
        System.out.println(l.distance("My string", "My $tring"));
        System.out.println(l.distance("My string", "My $tring"));

    }
    public static void levenshtein(){
        Levenshtein l = new Levenshtein();
        System.out.println(l.distance("My string", "My $tring"));
        System.out.println(l.distance("My string", "My $tring"));
        System.out.println(l.distance("My string", "My $tring"));

    }
    public static void weightedLevenshtein(){
        WeightedLevenshtein wl = new WeightedLevenshtein(
                new CharacterSubstitutionInterface() {
                    public double cost(char c1, char c2) {

                        // The cost for substituting 't' and 'r' is considered
                        // smaller as these 2 are located next to each other
                        // on a keyboard
                        if (c1 == 't' && c2 == 'r') {
                            return 0.5;
                        }

                        // For most cases, the cost of substituting 2 characters
                        // is 1.0
                        return 1.0;
                    }
                });

        System.out.println(wl.distance("String1", "Srring2"));
    }
    public  static void damerauLevenshtein(){
        Damerau d = new Damerau();

        // 1 substitution
        System.out.println(d.distance("ABCDEF", "ABDCEF"));

        // 2 substitutions
        System.out.println(d.distance("ABCDEF", "BACDFE"));

        // 1 deletion
        System.out.println(d.distance("ABCDEF", "ABCDE"));
        System.out.println(d.distance("ABCDEF", "BCDEF"));
        System.out.println(d.distance("ABCDEF", "ABCGDEF"));

        // All different
        System.out.println(d.distance("ABCDEF", "POIU"));
    }
    public static void optimalStringAlignment(){
        OptimalStringAlignment osa = new OptimalStringAlignment();

        System.out.println(osa.distance("CA", "ABC"));;
    }
    public static void jaroWinkle(){
        JaroWinkler jw = new JaroWinkler();

        // substitution of s and t
        System.out.println(jw.similarity("My string", "My tsring"));

        // substitution of s and n
        System.out.println(jw.similarity("My string", "My ntrisg"));
    }
    public static void longestCommonSubsequence(){
        LongestCommonSubsequence lcs = new LongestCommonSubsequence();

        // Will produce 4.0
        System.out.println(lcs.distance("AGCAT", "GAC"));

        // Will produce 1.0
        System.out.println(lcs.distance("AGCAT", "AGCT"));
    }
    public static void metricLCS(){
        info.debatty.java.stringsimilarity.MetricLCS lcs =
                new info.debatty.java.stringsimilarity.MetricLCS();

        String s1 = "ABCDEFG";
        String s2 = "ABCDEFHJKL";
        // LCS: ABCDEF => length = 6
        // longest = s2 => length = 10
        // => 1 - 6/10 = 0.4
        System.out.println(lcs.distance(s1, s2));

        // LCS: ABDF => length = 4
        // longest = ABDEF => length = 5
        // => 1 - 4 / 5 = 0.2
        System.out.println(lcs.distance("ABDEF", "ABDIF"));
    }
    public static void nGram(){
        NGram twogram = new NGram(2);
        System.out.println(twogram.distance("ABCD", "ABTUIO"));

        // produces 0.97222
        String s1 = "Adobe CreativeSuite 5 Master Collection from cheap 4zp";
        String s2 = "Adobe CreativeSuite 5 Master Collection from cheap d1x";
        NGram ngram = new NGram(4);
        System.out.println(ngram.distance(s1, s2));
    }
    public static void qGram(){
        QGram dig = new QGram(2);

        // AB BC CD CE
        // 1  1  1  0
        // 1  1  0  1
        // Total: 2

        System.out.println(dig.distance("ABCD", "ABCE"));
    }
   /* public static void kShingling(){
        String s1 = "My first string";
        String s2 = "My other string...";

        // Let's work with sequences of 2 characters...
        KShingling ks = new KShingling(2);

        // For cosine similarity I need the profile of strings
        StringProfile profile1 = ks.getProfile(s1);
        StringProfile profile2 = ks.getProfile(s2);

        // Prints 0.516185
        System.out.println(profile1.cosineSimilarity(profile2));
    }*/
}