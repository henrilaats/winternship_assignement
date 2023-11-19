package main.java.com.betting.util;

import main.java.com.betting.model.Match;
import main.java.com.betting.model.Player;
import main.java.com.betting.model.PlayerAction;
import main.java.com.betting.model.Casino;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileParser {

    public static Map<String, Match> readMatchData(String filename) {
        Map<String, Match> matches = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String matchId = parts[0];
                double rateA = Double.parseDouble(parts[1]);
                double rateB = Double.parseDouble(parts[2]);
                String result = parts[3];
                matches.put(matchId, new Match(matchId, rateA, rateB, result));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return matches;
    }
    public static LinkedHashMap<String, Player> readPlayerData(String filename) {
        LinkedHashMap<String, Player> players = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String playerId = parts[0].strip();
                Player player = players.computeIfAbsent(playerId, Player::new);

                String operation = parts[1].strip();
                String matchId = (parts.length > 2 && !parts[2].isEmpty()) ? parts[2] : null;
                int amount = (parts.length > 3) ? Integer.parseInt(parts[3]) : 0;
                String side = (parts.length > 4 && !parts[4].isEmpty()) ? parts[4].strip() : null;

                PlayerAction playerAction = new PlayerAction(playerId, operation,matchId, amount, side);
                player.getPlayerActions().add(playerAction);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return players;
    }

    public static void writeResults(String filename, LinkedHashMap<String, Player> players, LinkedHashMap<String, PlayerAction> illegalPlayers, Casino casino) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/java/com/betting/" + filename), StandardCharsets.UTF_8))) {
            writeLegitPlayers(bw, players);
            bw.newLine();
            writeIllegalPlayers(bw, illegalPlayers);
            bw.newLine();
            bw.write(String.valueOf(casino.getBalance()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void writeLegitPlayers(BufferedWriter bw, LinkedHashMap<String, Player> players) throws IOException {
        if (players.isEmpty()) bw.newLine();

        Collections.sort(new LinkedList<>(players.values()));
        for (Player player : players.values()) {
            bw.write(player.toString());
        }
        bw.newLine();
    }
    private static void writeIllegalPlayers(BufferedWriter bw, LinkedHashMap<String, PlayerAction> players) throws IOException {
        if (players.isEmpty()) bw.newLine();

        Collections.sort(new LinkedList<>(players.values()));
        for (PlayerAction action : players.values()) {
            bw.write(action.getPlayerId() + " " + action.getOperation() + " " + action.getMatchId() +  " " +
                    action.getAmount() + " " + action.getSide());
        }
        bw.newLine();
    }
}