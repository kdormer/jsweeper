package uk.ac.glos.CT5025.S1802423;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private static Player testPlayer;
    private static PlayerWriter mockPr;

    @BeforeAll
    static void prInit() {
        mockPr = new PlayerWriter();
    }

    @BeforeEach
    void setUp() {
        // Initialise new Player object prior to each test
        testPlayer = new Player("00000", "Kyle", 50, 7, 4, 3);
        mockDataFile();

    }

    @Test
    void getAllPlayers() {
        assertNotNull(Player.getAllPlayers());
    }

    @Test
    void getLeaderboard() {
        assertNotNull(Player.getLeaderboard());
    }

    @Test
    void sortHashMap() {
        HashMap<String, Integer> mockLeaderboard = Player.getLeaderboard();
        List<Map.Entry<String, Integer>> list = new ArrayList<>(mockLeaderboard.entrySet());
        Iterator<Map.Entry<String, Integer>> it = list.iterator();

        boolean sorted = true;
        int first = list.get(0).getValue();

        /*
         * If subsequent values are higher than first value
         * then HashMap is not properly sorted.
         */
        while(it.hasNext()) {
            if(it.next().getValue() > first) {
                sorted = false;
            }
        }

        assertTrue(sorted, "Expect hashmap to be sorted");
    }

    @Test
    void testId() {
        String testId = (new Player("Test")).getId();

        // If value is null then NoSuchAlgorithmException was triggered in method body
        assertNotNull(testId);

        // Length of new Player IDs should be 5
        assertEquals(5, testId.length());

        // Ensure collision rate for 10 new IDs is passable
        ArrayList<String> ids = new ArrayList<>(10);
        boolean duplicateId = false;

        for(int i = 0; i < 10; i++) {
            ids.add(mockPr.getID("TEST"));
        }

        /*
         * If Set length is same as ArrayList length then all
         * values are unique - no collisions.
         */
        Set<String> set = new HashSet<>(ids);

        if(set.size() < ids.size()) {
            duplicateId = true;
        }

        assertFalse(duplicateId);
    }

    @Test
    void getId() {
        assertNotNull(testPlayer.getId());
        assertEquals(testPlayer.getId().length(), 5);
    }

    @Test
    void setId() {
        testPlayer.setId("55555");
        assertEquals(testPlayer.getId(), "55555");
    }

    @Test
    void getName() {
        assertNotNull(testPlayer.getName());
        assertTrue(testPlayer.getName().length() >= 1);
        assertTrue(testPlayer.getName().length() <= 8);
    }

    @Test
    void setName() {
        testPlayer.setName("TEST");
        assertEquals(testPlayer.getName(), "TEST");
    }

    @Test
    void setHighScore() {
        testPlayer.setHighScore(10);
        assertEquals(10, testPlayer.getHighScore());
    }

    @Test
    void setWinCount() {
        testPlayer.setWinCount(50);
        assertEquals(50, testPlayer.getWinCount());
    }

    @Test
    void setLossCount() {
        testPlayer.setLossCount(50);
        assertEquals(50, testPlayer.getLossCount());
    }

    @Test
    void setScore() {
        testPlayer.setScore(500);
        assertEquals(500, testPlayer.getScore());
    }

    @Test
    void addScore() {
        testPlayer.setScore(30);
        testPlayer.addScore(40);
        assertEquals(70, testPlayer.getScore());
    }

    @Test
    void setPlayerTurn() {
        testPlayer.setPlayerTurn(true);
        assertTrue(testPlayer.isPlayerTurn());
    }

    @Test
    void switchPlayerTurn() {
        testPlayer.setPlayerTurn(false);
        testPlayer.switchPlayerTurn();
        assertTrue(testPlayer.isPlayerTurn());
    }

    @Test
    void setGamesPlayed() {
        testPlayer.setGamesPlayed(200);
        assertEquals(200, testPlayer.getGamesPlayed());
    }

    private void mockDataFile() {
        // Create Player objects
        Player p1 = new Player("00001", "Homer", 43, 12, 6, 6);
        Player p2 = new Player("00002", "Seneca", 39, 12, 6, 6);
        Player p3 = new Player("00003", "Augustus", 33, 12, 6, 6);
        Player p4 = new Player("00004", "Agrippa", 24, 12, 6, 6);
        Player p5 = new Player("00005", "Marcus", 24, 12, 6, 6);

        // Store Players in XML
        p1.storePlayer();
        p2.storePlayer();
        p3.storePlayer();
        p4.storePlayer();
        p5.storePlayer();
    }
}