import edu.duke.FileResource;
import org.apache.commons.csv.CSVRecord;

import java.util.*;

public class RatingsLogic {

    public ArrayList<Movie> loadMovies(String filename){
        ArrayList<Movie> movieList = new ArrayList<>();
        FileResource fr = new FileResource(filename);
        for (CSVRecord record : fr.getCSVParser()){
            movieList.add(new Movie(record.get(0), record.get(1), record.get(2),
                    record.get(4), record.get(5), record.get(3),
                    record.get(7), Integer.parseInt(record.get(6))));
        }
        return movieList;
    }

    private int dotProduct (Rater user, Rater other){
        ArrayList<String> userItems = user.getItemsRated();
        ArrayList<String> otherItems = other.getItemsRated();
        ArrayList<String> duplicateItems = new ArrayList<>();
        ArrayList<String> uniqueItems = new ArrayList<>();
        int dotProduct = 0;
        for (String movieID : userItems){
            if (otherItems.contains(movieID)){
                duplicateItems.add(movieID);
            }
            else{
                uniqueItems.add(movieID);
            }
        }
        for (String movieID : otherItems){
            if (otherItems.contains(movieID)){
                duplicateItems.add(movieID);
            }
            else{
                uniqueItems.add(movieID);
            }
        }
        HashMap<String, Double> userRatings = new HashMap<>();
        HashMap<String, Double> otherRatings = new HashMap<>();
        for (String movieID : duplicateItems){
            double currUserRating = user.getRating(movieID);
            double currOtherRating = other.getRating(movieID);
            userRatings.put(movieID, currUserRating);
            otherRatings.put(movieID, currOtherRating);
        }
        for (String movieID : uniqueItems){
            double currUserRating = user.getRating(movieID);
            double currOtherRating = other.getRating(movieID);
            userRatings.put(movieID, currUserRating);
            otherRatings.put(movieID, currOtherRating);
        }

        for (String movieID : userRatings.keySet()){
            if (userRatings.get(movieID) == -1 || otherRatings.get(movieID) == -1){
                continue;
            }
            dotProduct += ((userRatings.get(movieID) - 5.0) * (otherRatings.get(movieID) - 5.0));
        }
        return dotProduct;
    }

    private ArrayList<Rating> getSimilarities (String raterID){
        ArrayList<Rating> list = new ArrayList<>();
        Rater user = RaterDatabase.getRater(raterID);
            for (Rater r : RaterDatabase.getRaters()){
                if (r != user){
                    list.add(new Rating(r.getID(), dotProduct(user, r)));
                }
            }
        Collections.sort(list, Collections.reverseOrder());
        return list;
    }

    public ArrayList<Rating> getSimilarRatingsByFilter (String raterID, int numSimilarRaters, int minimalRaters, Filter filterCriteria){
        ArrayList<Rating> output = new ArrayList<>();
        ArrayList<Rating> list = getSimilarities(raterID);
        for (String movieID : MovieDatabase.filterBy(filterCriteria)){
            double runningTotal = 0.0;
            int nRatings = 0;
            for (int i = 0; i < numSimilarRaters; i++) {
                Rating r = list.get(i);
                double similarRating = RaterDatabase.getRater(r.getItem()).getRating(movieID);
                if (similarRating != -1) {
                    runningTotal += (r.getValue() * similarRating);
                    nRatings += 1;
                }
            }
            if (nRatings >= minimalRaters){
                output.add(new Rating(movieID, runningTotal/nRatings));
            }
        }
        Collections.sort(output, Collections.reverseOrder());
        return output;
    }

}


