package Profiles;

import java.util.ArrayList;

public class Profile {

    protected int ID;
    protected double genomeRatings[];
    protected double genres[];

    public Profile(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    public double[] getGenomeRatings() {
        return this.genomeRatings;
    }

    public double[] getGenres() {
        return this.genres;
    }

    public void importProfile(ArrayList<String> profileList){
        this.ID = Integer.parseInt(profileList.get(0));

        int totalGenomes = profileList.get(1).split("\\|").length;
        int totalGenres = profileList.get(2).split("\\|").length;
        this.genomeRatings = new double[totalGenomes];
        this.genres = new double[totalGenres];

        int i = 0;
        // Import genome ratings
        for (String genomeRating : profileList.get(1).split("\\|")){
            this.genomeRatings[i++] = Double.parseDouble(genomeRating);
        }

        i = 0;
        // Import genres
        for (String genreRating : profileList.get(2).split("\\|")){
            this.genres[i++] = Double.parseDouble(genreRating);
        }
    }

    public double[] convertToArray(){
        int size = this.genomeRatings.length + this.genres.length + 1;

        double[] output = new double[size];

        // Save ID
        output[0] = this.getID();

        // Save genomeRatings
        System.arraycopy(this.genomeRatings, 0, output, 1, this.genomeRatings.length);

        // Save genres
        System.arraycopy(this.genres, 0, output, genomeRatings.length+1, this.genres.length);

        return output;
    }
}
