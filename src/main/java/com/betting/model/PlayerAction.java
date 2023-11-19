package main.java.com.betting.model;

public class PlayerAction implements Comparable<PlayerAction>{
    private String playerId;
    private String operation;
    private String matchId;
    private int amount;
    private String side;

    public PlayerAction(String playerId, String operation, String matchId, int amount, String side) {
        this.playerId = playerId;
        this.operation = operation;
        this.matchId = matchId;
        this.amount = amount;
        this.side = side;
    }

    public String getPlayerId() {return playerId;}
    public String getOperation() {
        return operation;
    }
    public String getMatchId() {
        return matchId;
    }
    public int getAmount() {
        return amount;
    }
    public String getSide() {
        return side;
    }

    @Override
    public int compareTo(PlayerAction other) {
        return this.playerId.compareTo(other.playerId);
    }
}