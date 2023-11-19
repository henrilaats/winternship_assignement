package main.java.com.betting;

import main.java.com.betting.model.*;
import main.java.com.betting.controller.CasinoController;
import static main.java.com.betting.util.FileParser.*;
import main.java.com.betting.util.FileParser;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        try {
            Casino casino = new Casino();
            Map<String, Match> matches = readMatchData("src/resources/match_data.txt");
            LinkedHashMap<String, Player> players = readPlayerData("src/resources/player_data.txt");

            LinkedHashMap<String, PlayerAction> illegalPlayers = CasinoController.processPlayerActions(players, matches, casino);
            CasinoController.updateCasinoBalance(players, matches, casino);

            FileParser.writeResults("result.txt", players, illegalPlayers, casino);
            logger.log(Level.INFO, "Operations succeeded!");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred: " + e.getMessage(), e);
        }
    }
}