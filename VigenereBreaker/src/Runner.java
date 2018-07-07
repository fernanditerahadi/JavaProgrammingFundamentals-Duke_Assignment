import java.util.*;

public class Runner {

    public void printOut(String text){
        String[] words = text.split("\\s+");
        int psize = 0;
        System.out.println("----------------------------------------------------------------");
        for(int k=0; k < words.length; k++){
            System.out.print(words[k]+ " ");
            psize += words[k].length() + 1;
            if (psize > 90) {
                System.out.println();
                psize = 0;
            }
        }
        System.out.println("\n--------------------------------------------------------------");
    }
    public void testBreakVigenere(){
        VigenereBreaker vb = new VigenereBreaker();
        ArrayList<Object> output = vb.breakVigenere();
        String decrypted = (String) output.get(0);
        int wordCounts = (int) output.get(1);
        int[] key = (int[]) output.get(2);
        String lang = (String) output.get(3);
        System.out.println("\nDecrypted Message: "+"\n");
        printOut(decrypted);
        System.out.println("No of valid words: "+wordCounts);
        System.out.println("Key: "+Arrays.toString(key)+"\nKey Length: "+key.length);
        System.out.println("Language: "+ lang);
    }

}
