package main.java.com.betting.controller;

import main.java.com.betting.model.Match;
import main.java.com.betting.model.Player;
import main.java.com.betting.model.PlayerAction;
import main.java.com.betting.model.Casino;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
public class CasinoController {
    public static LinkedHashMap<String, PlayerAction> processPlayerActions(Map<String, Player> players, Map<String, Match> matches, Casino casino) {
        LinkedHashMap<String, PlayerAction> illegalPlayer = new LinkedHashMap<>();
        for (Player player : players.values()) {
            for (PlayerAction action : player.getPlayerActions()) {
                try {
                    player.performAction(action, matches);
                } catch (Exception e) {
                    if (!illegalPlayer.containsKey(player.getId())) {
                        illegalPlayer.put(player.getId(), action);
                    }
                }
            }
        }
        for (String id : illegalPlayer.keySet()) {
            players.remove(id);
        }
        return illegalPlayer;
    }
    public static void updateCasinoBalance(Map<String, Player> players, Map<String, Match> matches, Casino casino) {
        for (Player player: players.values()) {
            for (PlayerAction action: player.getPlayerActions()) {
                if (action.getOperation().equals("BET")) {
                    casino.processBet(action, matches);
                }
            }
        }
    }
}