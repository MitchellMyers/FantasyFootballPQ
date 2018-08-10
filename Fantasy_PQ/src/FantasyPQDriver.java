/**
 * Created by Mitch on 8/9/18.
 */
public class FantasyPQDriver {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("You must add your path to the fantasy_rankings directory as a command line arg");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]);
            if (i != args.length - 1) {
                sb.append(" ");
            }
        }
        String filePath = sb.toString();
        FantasyPQ fantasyPriorityQueue = new FantasyPQ();
        fantasyPriorityQueue.writePriorityFile(filePath);
        System.out.println("Done!");
    }



}
