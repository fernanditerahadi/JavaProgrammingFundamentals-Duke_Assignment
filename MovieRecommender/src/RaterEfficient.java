import java.util.ArrayList;
import java.util.HashMap;

public class RaterEfficient implements Rater{
    private String myID;
    private HashMap<String, Rating> myRatings;

    public RaterEfficient(String id) {
        myID = id;
        myRatings = new HashMap<>();
    }

    public void addRating(String item, double rating) {
        myRatings.put(item, new Rating(item, rating));
    }

    public boolean hasRating(String item) {
        if (myRatings.containsKey(item)) {
            return true;
        }
        return false;
    }

    public String getID() {
        return myID;
    }

    public double getRating(String item) {
        if (hasRating(item)) {
            return myRatings.get(item).getValue();
        }
        return -1;
    }

    public int numRatings() {
        return myRatings.size();
    }

    public ArrayList<String> getItemsRated() {
        ArrayList<String> list = new ArrayList<>();
        for (String movieID : myRatings.keySet()){
            list.add(myRatings.get(movieID).getItem());
        }
        return list;
    }
}
