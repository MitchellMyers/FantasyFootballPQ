import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Created by Mitch on 8/9/18.
 */
public class FantasyPQ {

    public void writePriorityFile(String filePath) {
        File[] rankingsFiles = getRankingFiles(filePath);
        Hashtable<String, RankFreq> rankingMasterTable = getPlayerRankingHashtable(rankingsFiles);
        PriorityQueue<Player> playerPriorityQueue = getPlayerPriorityQueue(rankingMasterTable);
        writePriorityQueueToFile(playerPriorityQueue, filePath);
    }

    private File[] getRankingFiles(String filePath) {
        return new File(filePath).listFiles();
    }

    private Hashtable<String, RankFreq> getPlayerRankingHashtable(File[] rankingFiles) {
        Hashtable<String, RankFreq> rankingMasterTable = new Hashtable<>();
        for (File rankingFile: rankingFiles) {
            if (!rankingFile.getName().equals("WeightedPlayerPriorities.csv")) {
                parseRankingFile(rankingFile, rankingMasterTable);
            }
        }
        return rankingMasterTable;
    }

    private void parseRankingFile(File rankingFile, Hashtable<String, RankFreq> rankingTable) {
        Scanner fileScanner;
        try {
            fileScanner = new Scanner(rankingFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        fileScanner.nextLine();
        while (fileScanner.hasNextLine()) {
            String rankingLine = fileScanner.nextLine();
            String[] rankSplit = rankingLine.split(",");
            addRankingToTable(rankSplit[2], Integer.parseInt(rankSplit[0]), rankingTable);
        }

    }

    private void addRankingToTable(String playerName, Integer rank, Hashtable<String, RankFreq> rankingTable) {
        RankFreq currRank = rankingTable.get(playerName);
        if (currRank == null) {
            rankingTable.put(playerName, new RankFreq(rank, 1));
        } else {
            currRank.setRank(currRank.getRank() + rank);
            currRank.setFreq(currRank.getFreq() + 1);
        }
    }

    private Comparator<Player> getPlayerComparator() {
        return new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                if (p1.getPlayerWeightedRank() < p2.getPlayerWeightedRank()) return -1;
                else if (p1.getPlayerWeightedRank() > p2.getPlayerWeightedRank()) return 1;
                else return 0;
            }
        };
    }

    private PriorityQueue<Player> getPlayerPriorityQueue(Hashtable<String, RankFreq> playerRankingTable) {
        PriorityQueue<Player> playerPriorityQueue = new PriorityQueue<>(getPlayerComparator());
        for (String playerName: playerRankingTable.keySet()) {
            RankFreq playerRankFreq = playerRankingTable.get(playerName);
            double weightedPlayerRank = playerRankFreq.getRank() / (double) playerRankFreq.getFreq();
            playerPriorityQueue.add(new Player(playerName, weightedPlayerRank));
        }
        return playerPriorityQueue;
    }

    private void writePriorityQueueToFile(PriorityQueue<Player> playerPriorityQueue, String filePath) {
        File weightedPriorityFile = new File(filePath + "/WeightedPlayerPriorities.csv");
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(weightedPriorityFile, false);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        while (!playerPriorityQueue.isEmpty()) {
            Player currPlayer = playerPriorityQueue.poll();
            String writtenLine = "" + String.valueOf(currPlayer.getPlayerWeightedRank()) + ","
                    + currPlayer.getPlayerName() +  "\n";
            try {
                fileWriter.append(writtenLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
