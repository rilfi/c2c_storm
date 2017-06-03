package experiment;

/**
 * Created by s1 on 5/11/2017.
 */
public class AlphaNuTest {
    public static void main(String[] args) {
        String text="GARY GRIMM &amp; ASSOCIATES";
        text=text.replaceAll("[\\s\\p{Z}]+", " ").trim();
        for (String word:text.split(" ")){
            if(!isAlphanumeric(word)){
                System.out.println(word);
            }
        }



    }
    public static boolean isAlphanumeric(String str) {
        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isDigit(c) && !Character.isLetter(c))
                return false;
        }

        return true;
    }

}
