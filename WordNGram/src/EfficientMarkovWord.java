import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class EfficientMarkovWord implements IMarkovModel{
    private HashMap<Integer, ArrayList<Object>> myMap;
    private String[] myText;
    private Random myRandom;
    private int myOrder;
    public EfficientMarkovWord(int order){
        myMap = new HashMap<>();
        myRandom = new Random();
        myOrder = order;
    }

    public void setTraining(String text) {
        myText = text.split("\\s+");
    }

    public void setRandom(int seed) {
        myRandom = new Random(seed);
    }

    public HashMap<Integer, ArrayList<Object>> buildMap(){
        WordGram lastString = new WordGram(myText, myText.length-myOrder, myOrder);
        Integer lastHash = lastString.hashCode();
        ArrayList<Object> lastObject = new ArrayList<>();
        ArrayList<String> lastFollows = new ArrayList<>();
        lastObject.add(0, lastString.toString());
        lastObject.add(1, lastFollows);
        myMap.put(lastHash, lastObject);
        for (int i = 0; i < myText.length-myOrder; i++) {
            WordGram currString = new WordGram(myText, i, myOrder);
            Integer currHash = currString.hashCode();
            String nextString = myText[i+myOrder];
            ArrayList<Object> object = new ArrayList<>();
            ArrayList<String> temp = new ArrayList<>();
            if (!myMap.containsKey(currHash)){
                temp.add(nextString);
                object.add(0, currString.toString());
                object.add(1, temp);
                myMap.put(currHash,object);
            }
            else{
                object = myMap.get(currHash);
                temp = (ArrayList<String>) object.get(1);
                temp.add(nextString);
                object.add(1, temp);
                myMap.put(currHash, object);
            }
        }
        printHashMapInfo();
        return myMap;
    }

    public ArrayList<Object> getFollowsInMap(WordGram kGram){
        return myMap.get(kGram.hashCode());
    }


    public String getRandomText(int numWords) {
        if (myText.length == 0){
            return "";
        }
        if (myMap.isEmpty()){
            buildMap();
        }
        StringBuilder sb = new StringBuilder();
        int index = myRandom.nextInt(myText.length-myOrder);
        WordGram keyGram = new WordGram(myText, index, myOrder);
        for (int i = 0; i < keyGram.length(); i++) {
            sb.append(keyGram.wordAt(i)).append(" ");
        }
        for (int j = 0; j < numWords-myOrder; j++) {
            ArrayList<String> follows = (ArrayList<String>) getFollowsInMap(keyGram).get(1);
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

    private void printHashMapInfo(){
        Integer notableKeyInt = null;
        String notableKeyStr = null;
        int largestValue = 0;
        for (Integer key : myMap.keySet()){
            String kGram = (String) myMap.get(key).get(0);
            ArrayList<String> follows = (ArrayList<String>) myMap.get(key).get(1);
            Integer currSize = follows.size();
            System.out.println(key+" - "+kGram+" - "+follows+" - "+currSize);
            if( currSize > largestValue){
                largestValue = currSize;
                notableKeyInt = key;
                notableKeyStr = kGram;
            }
        }
        System.out.println("Number of keys: "+myMap.size());
        System.out.println("Largest Value in the HashMap: "+largestValue);
        System.out.println("The most notable key: "+notableKeyInt+" - "+notableKeyStr);
    }

    public String toString(){
        return getClass().getName()+" "+myOrder;
    }
}
