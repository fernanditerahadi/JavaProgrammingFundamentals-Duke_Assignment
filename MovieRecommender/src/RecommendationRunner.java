import java.util.ArrayList;
import java.util.Random;

public class RecommendationRunner implements Recommender {
    private Random myRandom;
    private static final int toRateNum = 15;
    private static final int year = 2000;
    public RecommendationRunner(){
        myRandom = new Random();
    }

    @Override
    public ArrayList<String> getItemsToRate(){
        MovieDatabase.initialize("ratedmoviesfull.csv");
        RaterDatabase.initialize("ratings.csv");
        ArrayList<String> movieList = MovieDatabase.filterBy(new FilterYearAfter(year));
        ArrayList<String> toRate = new ArrayList<>();
        for (int i = 0; i < toRateNum; i++) {
            int currIndex = myRandom.nextInt(movieList.size());
            String movieID = movieList.get(currIndex);
            toRate.add(movieID);
        }
        System.out.println("<h3 style='text-align: center'>Please rate the following "+toRate.size()+" movies</h3>");
        return toRate;
    }

    @Override
    public void printRecommendationsFor(String webRaterID) {
        int count = 0;
        RatingsLogic rl = new RatingsLogic();
        ArrayList<Rating> list = rl.getSimilarRatingsByFilter(webRaterID, 20, 3, new FilterYearAfter(year));
        if (list.size() > 0){
            System.out.println("<html><h3 style='text-align: center'>Suggested Movies by Similarity to Other Users' Preferences</h3>");
            System.out.println("<style>" +
                    "table{" +
                        "margin: 1% auto;" +
                        "width: 85%;" +
                        "border-collapse: collapse;}" +
                    "th, tr, td{" +
                        "border: 1px solid black;" +
                        "padding: 0.5% 1%;}" +
                    "tr:nth-child(odd){" +
                        "background-color: #E0E0E0;}" +
                    "tr:nth-child(even){" +
                        "background-color: #F2F2F2;}" +
                    "</style>" +
                    "<table>" +
                    "<tr>" +
                    "<th></th>" +
                    "<th>Movie</th>" +
                    "<th>Genre</th>" +
                    "<th>Director</th>" +
                    "<th>Rating</th>" +
                    "</tr>");
            for (Rating r : list){
                count += 1;
                String movieID = r.getItem();
                int rating = (int) r.getValue();
                int year = MovieDatabase.getYear(movieID);
                String poster = MovieDatabase.getPoster(movieID);
                String title = MovieDatabase.getTitle(movieID);
                String genre = MovieDatabase.getGenres(movieID);
                String director = MovieDatabase.getDirector(movieID);
                String space = "&nbsp; &nbsp;";
                System.out.println("<tr>" +
                        "<td><img src='"+poster+"' height='50' align='right'></td>" +
                        "<td>"+year+space+"<a href='"+poster+"'>"+title+"</a></td>" +
                        "<td>"+genre+"</td>" +
                        "<td>"+director+"</td>" +
                        "<td>"+rating+space+space+space+"</td>" +
                        "</tr>");
                if (count == 10){
                    break;
                }
            }
            System.out.println("</table></html>");
        }
        else{
            System.out.println("<html><h3 style='text-align: center'>There are no suggested movies to be shown</h3>");
            System.out.println("<h2 style='text-align: center'>Please come back and rate more movies</h2>");
        }
    }
}
