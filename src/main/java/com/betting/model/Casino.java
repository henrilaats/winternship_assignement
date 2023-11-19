package main.java.com.betting.model;

import java.util.Map;
public class Casino {
    private long balance;
    public Casino() {
        this.balance = 0;
    }
    private boolean validatePlayerSide(String side) {
        return side.equals("A") || side.equals("B");
    }
    public long getBalance() {
        return balance;
    }
    public void processBet(PlayerAction playerAction, Map<String, Match> matches) {
        if (playerAction == null || matches == null) return;

        String matchID = playerAction.getMatchId();
        String playerSide = playerAction.getSide();

        if (matches.containsKey(matchID)) {
            Match match = matches.get(matchID);

            if (match != null) {
                String matchResult = match.getResult();
                double matchRate = (playerSide.equals("A")) ? match.getRateA() : match.getRateB();

                if (validatePlayerSide(playerSide)) {
                    if (playerSide.equals(matchResult)) {
                        balance -= (long) (playerAction.getAmount() *  matchRate);
                    } else if (!matchResult.equals("DRAW")) {
                        balance += playerAction.getAmount();
                    }
                }
            }
        }
    }
}