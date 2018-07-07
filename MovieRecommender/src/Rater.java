import java.util.ArrayList;

public interface Rater {
    void addRating(String item, double rating);
    boolean hasRating(String item);
    double getRating(String item);
    ArrayList<String> getItemsRated();
    String getID();
    int numRatings();
}
