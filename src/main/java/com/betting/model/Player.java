package main.java.com.betting.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Player implements Comparable<Player>{
    private String id;
    private long balance;
    private List<PlayerAction> playerActions;
    private int nrOfBets;
    private int wins;

    public Player(String id) {
        this.id = id;
        this.balance = 0;
        this.playerActions = new LinkedList<>();
        this.nrOfBets = 0;
        this.wins = 0;
    }

    public String getId() {
        return id;
    }
    public long getBalance() {
        return balance;
    }
    public List<PlayerAction> getPlayerActions() {
        return playerActions;
    }

    public void performAction(PlayerAction playerAction,Map<String, Match> matches) {
        if (playerAction == null || matches == null) return;

        String action = playerAction.getOperation();
        switch (action) {
            case "DEPOSIT" -> deposit(playerAction.getAmount());
            case "WITHDRAW" -> withdraw(playerAction.getAmount());
            case "BET" -> bet(playerAction.getMatchId(), playerAction.getAmount(), playerAction.getSide(), matches);
        }
    }
    private void deposit(int amount) {
        if (amount<=0) return;
        this.balance += amount;
    }

    private void withdraw(int amount) {
        if (amount <= 0) return;
        if (balance >= amount) {
            this.balance -= amount;
        } else throw new RuntimeException("Illegal operation - withdrawal amount larger than balance.");
    }

    private void bet(String matchId, int amount, String side, Map<String, Match> matches) {
        Match match = matches.get(matchId);

        if (match == null) return;

        nrOfBets++;
        double rate = (side.equals("A")) ? match.getRateA() : match.getRateB();

        if (balance < amount) {
            throw new RuntimeException("Illegal operation - betting amount is larger than balance.");
        }

        if (side.equals(match.getResult())) {
            wins++;
            this.balance += (long) (amount * rate);
        } else if (!match.getResult().equals("DRAW")){
            this.balance -= amount;
        }
    }
    public BigDecimal getWinRate() {
        if (nrOfBets == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(wins).divide(BigDecimal.valueOf(nrOfBets),2, RoundingMode.HALF_UP);
    }
    @Override
    public String toString() {
        return this.id + " " + this.balance + " " + this.getWinRate();
    }
    @Override
    public int compareTo(Player other) {
        return this.id.compareTo(other.id);
    }
}