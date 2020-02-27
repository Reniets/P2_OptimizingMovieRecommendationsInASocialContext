package Profiles;

public class MovieProfile extends Profile{

    public MovieProfile(int ID, double[] genomeRatings, String[] genres){
        super(ID);

        this.genomeRatings = genomeRatings;
        this.genres = calcGenres(genres);
    }

    public MovieProfile(int ID){
        super(ID);
    }

    private double[] calcGenres(String[] genres){

        String[] genreCmp = {"Action", "Adventure", "Horror", "Childrens", "Romance", "War", "Western", "Documentary", "Sci-Fi", "Drama",
                "Thriller", "Crime", "Fantasy", "Animation", "Comedy", "Mystery", "Musical", "Film-Noir"};
        int totalGenres = genreCmp.length;

        double[] output = new double[totalGenres];

        for (int i = 0; i < totalGenres; i++){
            if (this.contains(genreCmp[i], genres)){
                output[i] = 1;
            } else {
                output[i] = 0;
            }
        }

        return output;
    }

    private boolean contains(String object, String[] list){

        for (String element : list) {
            if (element.equals(object)) {
                return true;
            }
        }

        return false;
    }
}
