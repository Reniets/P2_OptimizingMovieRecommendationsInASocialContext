package Profiles;

public class UserProfile extends Profile {

    private int totalRatings;


    public UserProfile(int ID, int totalGenomeTags, int totalGenres) {
        super(ID);

        this.totalRatings = 0;
        this.genomeRatings = new double[totalGenomeTags];
        this.genres = new double[totalGenres];
    }

    public void updateRatings(MovieProfile MP, double score) {

        this.totalRatings++;

        this.runningAverageUpdate(MP, score);
    }

    private void runningAverageUpdate(MovieProfile MP, double score) {
        double newRange = this.map_range(score, 0.2,1,-1,1);
        double adaptRate = 0.15 * newRange;

        // Update genome ratings
        int totalGenomoes = this.genomeRatings.length;
        for (int genomeID = 0; genomeID < totalGenomoes; genomeID++){
            this.genomeRatings[genomeID] += adaptRate * (MP.genomeRatings[genomeID] - this.genomeRatings[genomeID]);

            // Cap rating between -1 and 1
            this.genomeRatings[genomeID] = this.genomeRatings[genomeID] < -1 ? -1 : this.genomeRatings[genomeID];
            this.genomeRatings[genomeID] = this.genomeRatings[genomeID] > 1 ? 1 : this.genomeRatings[genomeID];
        }

        // Update genre ratings.
        int totalGenres = this.genres.length;
        for (int genreID = 0; genreID < totalGenres; genreID++){
            this.genres[genreID] += adaptRate * (MP.genres[genreID] - this.genres[genreID]);

            // Cap rating between -1 and 1
            this.genres[genreID] = this.genres[genreID] < -1 ? -1 : this.genres[genreID];
            this.genres[genreID] = this.genres[genreID] > 1 ? 1 : this.genres[genreID];
        }
    }

    private double map_range(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
