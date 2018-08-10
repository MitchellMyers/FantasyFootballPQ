import java.util.Comparator;

/**
 * Created by Mitch on 8/9/18.
 */
public class Player {

    private String playerName;
    private double weightedRank;

    public Player(String playerName, double weightedRank) {
        this.playerName = playerName;
        this.weightedRank = weightedRank;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public double getPlayerWeightedRank() {
        return this.weightedRank;
    }

}
