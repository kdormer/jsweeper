package uk.ac.glos.CT5025.S1802423;

import javax.swing.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Game {
    public Game() {
        this.displayGameMenu();
    }

    /**
     * Display game menu. Method is used recursively to deal with
     * invalid user input. Depending on user's input other methods
     * are procedurally called from here.
     */
    private void displayGameMenu() {
        this.displayOptions(new String[]{"1. Choose/create player profile", "2. Play as guest", "3. View/manage player information", "4. View leaderboard", "5. Exit"});
        int[] choice = this.getUserInput("Enter a choice: ", new int[1], 1, 5);

        switch (choice[0]) {
            case 1:
                this.displayOptions(new String[]{"1. Choose player profile", "2. Create new player profile"});
                choice = this.getUserInput("Enter a choice: ", new int[1], 1, 2);

                switch (choice[0]) {
                    case 1:
                        Player p1 = this.getPlayerChoice(1);
                        Player p2 = this.getPlayerChoice(2);

                        if (p1 != null && p2 != null) {
                            if (p1.getId().equals(p2.getId())) {

                                System.out.println("Only one player profile selected! Launching game against guest profile...");
                                this.launchGame(p1, new Player("Player 2"));

                            } else {
                                this.launchGame(p1, p2);
                            }

                        } else {
                            System.out.println("No player profiles found!");
                            this.displayGameMenu();
                        }
                        break;

                    case 2:
                        String[] name = this.getName(new String[1]);
                        Player newPlayer = new Player(name[0]);
                        newPlayer.storePlayer();
                        this.displayGameMenu();
                        break;
                }

                break;

            case 2:
                this.launchGame(new Player("Player 1"), new Player("Player 2"));
                break;
            case 3:
                if (Player.getAllPlayers().length > 0) {
                    Player player = this.getPlayerChoice(1);
                    this.displayPlayerMenu(player);
                } else {
                    System.out.println("No player profiles found!");
                    this.displayGameMenu();
                }
                break;
            case 4:
                if (Player.getAllPlayers().length > 0) {
                    this.displayLeaderboard();
                } else {
                    System.out.println("No players found!");
                    this.displayGameMenu();
                }
                break;
            case 5:
                System.exit(0);
                break;
        }
    }

    /**
     * Method used to display options in a menu.
     *
     * @param options Array of string options.
     */
    private void displayOptions(String[] options) {
        this.printSpacer();
        for (String option : options) {
            System.out.println(option);
        }
    }

    /**
     * Get user input. Recursively handles invalid inputs by reusing the same integer array.
     *
     * @param prompt String to prompt the user to enter input.
     * @param choice Integer array that will store user's valid input.
     * @param min    Minimum integer option allowed.
     * @param max    Maximum integer option allowed.
     * @return Integer array with valid user input as first element.
     */
    private int[] getUserInput(String prompt, int[] choice, int min, int max) {
        Scanner reader = new Scanner(System.in);
        System.out.println(prompt);

        try {
            int input = reader.nextInt();

            if (input >= min && input <= max) {
                choice[0] = input;
            } else {
                System.out.println("Invalid choice! Please try again!");
                this.getUserInput(prompt, choice, min, max);
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid choice! Please try again!");
            this.getUserInput(prompt, choice, min, max);
        }

        return choice;
    }

    /**
     * Get user's name. Similar to getUserInput() but returns string.
     *
     * @param name String array used to store valid user input and retain data when being used recursively.
     * @return String array with valid user input as first element.
     */
    private String[] getName(String[] name) {
        Scanner reader = new Scanner(System.in);
        this.printSpacer();
        System.out.println("Enter name: ");
        String input = reader.nextLine();

        if (input.length() >= 1 && input.length() <= 8) {
            name[0] = input;
        } else {
            System.out.println("Invalid name! Please ensure it is between 1 and 8 characters long!");
            this.getName(name);
        }

        return name;
    }

    /**
     * Prompts user to select a player from currently stored players.
     * Recursively handles invalid inputs using getUserInput().
     *
     * @param playerNumber Integer corresponding to which player is being asked for (1/2).
     * @return Player object created based on user's choice.
     */
    private Player getPlayerChoice(int playerNumber) {
        Player[] players = Player.getAllPlayers();
        int[] choice;
        int i = 0;

        if (players.length > 0) {
            for (Player player : players) {
                System.out.println((i + 1) + ". " + player.getName() + " [" + player.getId() + "]");
                i++;
            }

            choice = this.getUserInput("Please select a profile for player " + playerNumber + ": ", new int[1], 1, i);

            if (players[choice[0] - 1] != null) {
                return players[choice[0] - 1];
            }

        }
        return null;
    }

    /**
     * Prints spacer line for more readable command-line menu.
     */
    private void printSpacer() {
        System.out.println("-----------------------------");
    }

    /**
     * Displays current leaderboard of players by creating
     * a sorted hashmap with players as keys and their high-scores as values.
     */
    private void displayLeaderboard() {
        HashMap<String, Integer> leaderboard = Player.getLeaderboard();
        int i = 1;

        System.out.println("======== LEADERBOARD ========");

        for (final Map.Entry<String, Integer> e : leaderboard.entrySet()) {
            System.out.println(i + ". " + e.getKey() + ": " + e.getValue() + " points");
            i++;
        }

        this.displayGameMenu();
    }

    /**
     * Display menu allowing viewing/managing of Player information.
     * Recursively handles invalid inputs using getUserInput() method.
     *
     * @param player Player that has been selected for viewing/managing.
     */
    private void displayPlayerMenu(Player player) {
        this.displayOptions(new String[]{"1. View player statistics", "2. Update player name", "3. Delete player"});
        int[] choice = this.getUserInput("Enter a choice: ", new int[1], 1, 3);

        switch (choice[0]) {
            case 1:
                this.displayPlayerStats(player);
                break;
            case 2:
                String[] newName = this.getName(new String[1]);
                player.setName(newName[0]);
                player.storePlayer();
                break;
            case 3:
                player.deletePlayer();
                System.out.println("Player profile deleted successfully!");
                break;
        }

        this.displayGameMenu();
    }

    /**
     * Displays game statistics of a specified Player object.
     *
     * @param player Player object selected to have stats displayed.
     */
    private void displayPlayerStats(Player player) {
        System.out.println("===== " + player.getName().toUpperCase() + " ======");
        System.out.println("High score: " + player.getHighScore());
        System.out.println("Gamed played: " + player.getGamesPlayed());
        System.out.println("Wins: " + player.getWinCount());
        System.out.println("Losses: " + player.getLossCount());
    }

    /**
     * Prompt user to choose a difficulty for the game.
     * Recursively handles invalid inputs using getUserInput() method.
     *
     * @return Difficulty enum type corresponding with user's input.
     */
    private Difficulty getDifficulty() {
        this.displayOptions(new String[]{"1. Easy", "2. Medium", "3. Hard", "4. Impossible"});
        int[] choice = this.getUserInput("Select a difficulty: ", new int[1], 1, 4);

        switch (choice[0]) {
            case 1:
                return Difficulty.EASY;
            case 2:
                return Difficulty.MEDIUM;
            case 3:
                return Difficulty.HARD;
            case 4:
                return Difficulty.IMPOSSIBLE;
        }

        return null;
    }

    /**
     * Launches game using the Swing event-dispatching thread.
     *
     * @param p1 Player 1 object either set as guest or chosen by player.
     * @param p2 Player 2 object either set as guest or chosen by player.
     */
    private void launchGame(Player p1, Player p2) {
        Difficulty difficulty = this.getDifficulty();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameFrame(p1, p2, difficulty);
            }
        });
    }

    public static void main(String[] args) {
        Game game = new Game();
    }
}
