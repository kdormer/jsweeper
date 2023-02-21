package uk.ac.glos.CT5025.S1802423;

import java.util.HashMap;

public class Player {
    private String id;
    private String name;
    private int highScore;
    private int gamesPlayed;
    private int winCount;
    private int lossCount;
    private int score;
    private boolean playerTurn = false;
    private final boolean isGuest;
    private static final PlayerWriter pr = new PlayerWriter();

    /**
     * Constructor used for creation of entirely new Players.
     * Players created (and subsequently) used by this constructor
     * are viewed as guest players.
     * New players are stored and then obtained from XMl storage,
     * meaning this does not affect anything except Guest games.
     *
     * @param name Name of new Player.
     */
    public Player(String name) {
        this.id = pr.getID(name);
        this.name = name;
        this.gamesPlayed = 0;
        this.highScore = 0;
        this.winCount = 0;
        this.lossCount = 0;
        this.isGuest = true;
    }

    /**
     * Constructor used to create pre-existing Player objects
     * deserialized from XML storage.
     *
     * @param id          String player ID.
     * @param name        String player name.
     * @param highScore   Integer player high-score.
     * @param gamedPlayed Integer player total number of games played.
     * @param winCount    Integer number of games won.
     * @param lossCount   Integer number of games lost.
     */
    public Player(String id, String name, int highScore, int gamedPlayed, int winCount, int lossCount) {
        this.id = id;
        this.name = name;
        this.highScore = highScore;
        this.gamesPlayed = gamedPlayed;
        this.winCount = winCount;
        this.lossCount = lossCount;
        this.isGuest = false;
    }

    /**
     * Uses static PlayerWriter instance to return an array of all stored
     * Players in static context.
     *
     * @return Array of Player objects containing all stored players.
     */
    public static Player[] getAllPlayers() {
        return pr.getAll();
    }

    /**
     * Returns Hashmap representing player leaderboard.
     * Key = Player name [Player ID]
     * Value = Player high-score
     * Hashmap is sorted using PlayerWriter sortHashMap() method.
     *
     * @return Sorted hashmap representing player leaderboard.
     */
    public static HashMap<String, Integer> getLeaderboard() {
        Player[] players = Player.getAllPlayers();
        HashMap<String, Integer> leaderboard = new HashMap<>(players.length);

        for (Player player : players) {
            leaderboard.put(player.getName() + " [" + player.getId() + "]", player.getHighScore());
        }

        leaderboard = pr.sortHashMap(leaderboard);
        return leaderboard;
    }

    /**
     * Stores player object using PlayerWriter writePlayer() method.
     */
    public void storePlayer() {
        pr.writePlayer(this);
    }

    /**
     * Delete Player object from XML storage.
     */
    public void deletePlayer() {
        pr.deletePlayer(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    public int getLossCount() {
        return lossCount;
    }

    public void setLossCount(int lossCount) {
        this.lossCount = lossCount;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean isTurn) {
        this.playerTurn = isTurn;
    }

    public void switchPlayerTurn() {
        this.playerTurn = !this.playerTurn;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public boolean isGuest() {
        return isGuest;
    }
}