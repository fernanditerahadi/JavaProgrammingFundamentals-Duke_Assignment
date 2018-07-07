import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MarkovWord implements IMarkovModel{
    private String[] myText;
    private Random myRandom;
    private int myOrder;
    public MarkovWord(int order){
        myRandom = new Random();
        myOrder = order;
    }

    public void setTraining(String text) {
        myText = text.split("\\s+");
    }

    public void setRandom(int seed) {
        myRandom = new Random(seed);
    }

    public int indexOf(String[] words, WordGram target, int start){
        for (int i = start; i <= words.length-myOrder ; i++) {
            WordGram object = new WordGram(words, i, myOrder);
            if (target.equals(object)){
                return i;
            }
        }
        return -1;
    }

    public ArrayList<String> getFollows(String[] words, WordGram kGram){
        ArrayList<String> follows = new ArrayList<>();
        int pos = 0;
        while (pos < words.length){
            int start = indexOf(words, kGram, pos);
            if (start == -1 || start + myOrder > words.length - 1){
                break;
            }
            String next = words[start+myOrder];
            follows.add(next);
            pos = start + 1;
        }
        return follows;
    }

    public String getRandomText(int numWords) {
        StringBuilder sb = new StringBuilder();
        int index = myRandom.nextInt(myText.length-myOrder);
        WordGram keyGram = new WordGram(myText, index, myOrder);
        for (int i = 0; i < keyGram.length(); i++) {
            sb.append(keyGram.wordAt(i)).append(" ");
        }
        for (int j = 0; j < numWords-myOrder; j++) {
            ArrayList<String> follows = getFollows(myText, keyGram);
            if (follows.size() == 0){
                break;
            }
            index = myRandom.nextInt(follows.size());
            String next = follows.get(index);
            sb.append(next).append(" ");
            keyGram = keyGram.shiftAdd(next);
        }
        return sb.toString().trim();
    }

    public String toString(){
        return getClass().getName()+" "+myOrder;
    }
}
