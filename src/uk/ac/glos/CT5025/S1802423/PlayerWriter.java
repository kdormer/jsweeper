package uk.ac.glos.CT5025.S1802423;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class PlayerWriter {
    private File dataFile;
    private Document document;
    private DocumentBuilder builder;

    /**
     * Constructor method used to initialise XML DOM.
     */
    public PlayerWriter() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            this.dataFile = new File("player_data.xml");
            this.builder = factory.newDocumentBuilder();

            if (this.dataFile.exists() && this.dataFile.isFile()) {
                this.document = builder.parse(this.dataFile);
                this.document.getDocumentElement().normalize();

            } else {
                this.setNewDocument();
            }

        } catch (SAXException | ParserConfigurationException | IOException e) {
            this.setNewDocument();
        }
    }

    /**
     * Method used to get a Player array of all currently stored players.
     *
     * @return Array of Player objects containing all currently stored players.
     */
    public Player[] getAll() {
        ArrayList<Player> players = new ArrayList<>();
        NodeList nList = document.getElementsByTagName("player");

        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                players.add(this.deserializePlayer(element));

            }
        }

        return players.toArray(new Player[0]);
    }

    /**
     * Stores passed Player object in XML storage.
     *
     * @param player Player object to be stored.
     */
    public void writePlayer(Player player) {
        Element rootElement = this.getRootElement();
        Element playerElement = (Element) this.getPlayer(player);

        if (playerElement != null) {
            playerElement.getElementsByTagName("name").item(0).setTextContent(player.getName());
            playerElement.getElementsByTagName("highScore").item(0).setTextContent(Integer.toString(player.getHighScore()));
            playerElement.getElementsByTagName("gamesPlayed").item(0).setTextContent(Integer.toString(player.getGamesPlayed()));
            playerElement.getElementsByTagName("winCount").item(0).setTextContent(Integer.toString(player.getWinCount()));
            playerElement.getElementsByTagName("lossCount").item(0).setTextContent(Integer.toString(player.getLossCount()));
        } else {
            rootElement.appendChild(this.serializePlayer(player));
        }

        this.writeFile();
    }

    /**
     * Delete specified player from XML storage.
     *
     * @param player Player object corresponding with player to be deleted.
     */
    public void deletePlayer(Player player) {
        Element rootElement = this.getRootElement();
        rootElement.removeChild(this.getPlayer(player));
        this.writeFile();
    }

    /**
     * Returns root element of DOM XML.
     *
     * @return root Element object.
     */
    private Element getRootElement() {
        return (Element) document.getFirstChild();
    }

    /**
     * Initialise a new XML document, used in cases where a document does not already exist.
     */
    private void setNewDocument() {
        this.document = builder.newDocument();
        this.documentInit();
    }

    /**
     * Initialise XML DOM and write it to storage.
     */
    private void documentInit() {
        // Add root element to XML document
        Element rootElement = this.document.createElement("players");
        this.document.appendChild(rootElement);
        this.writeFile();
    }

    /**
     * Write XMl document to storage.
     */
    private void writeFile() {
        try {
            TransformerFactory trFactory = TransformerFactory.newInstance();
            Transformer transformer = trFactory.newTransformer();
            DOMSource source = new DOMSource(this.document);
            StreamResult result = new StreamResult(this.dataFile);
            transformer.transform(source, result);

        } catch (Exception ignored) {
        }
    }

    /**
     * Get specified player Node from stored XML document.
     *
     * @param player Player object specifying which node should be returned.
     * @return Node object from XML DOM containing specified player.
     */
    private Node getPlayer(Player player) {
        this.document.normalize();
        Node playerNode = null;
        NodeList nList = document.getElementsByTagName("player");

        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);

            if (node.getAttributes().getNamedItem("id").getTextContent().equals(player.getId())) {
                playerNode = node;
            }
        }

        return playerNode;
    }

    /**
     * Method to serialize a Player object to XML, ready for being stored.
     *
     * @param player Player object to be serialized.
     * @return Serialized player Element object.
     */
    private Element serializePlayer(Player player) {
        if (player != null) {
            Attr id = document.createAttribute("id");
            Element playerElement = document.createElement("player");
            Element name = document.createElement("name");
            Element highScore = document.createElement("highScore");
            Element gamesPlayed = document.createElement("gamesPlayed");
            Element winCount = document.createElement("winCount");
            Element lossCount = document.createElement("lossCount");

            id.setValue(player.getId());
            name.setTextContent(player.getName());
            highScore.setTextContent(Integer.toString(player.getHighScore()));
            gamesPlayed.setTextContent(Integer.toString(player.getGamesPlayed()));
            winCount.setTextContent((Integer.toString(player.getWinCount())));
            lossCount.setTextContent((Integer.toString(player.getLossCount())));

            playerElement.setAttributeNode(id);
            playerElement.setIdAttribute("id", true);
            playerElement.appendChild(name);
            playerElement.appendChild(highScore);
            playerElement.appendChild(gamesPlayed);
            playerElement.appendChild(winCount);
            playerElement.appendChild(lossCount);

            return playerElement;
        }
        return null;
    }

    /**
     * Method used to deserialize player Element and return Player object.
     *
     * @param playerElement Player Element to be deserialized.
     * @return Deserialized Player object.
     */
    private Player deserializePlayer(Element playerElement) {
        String id;
        String name = "";
        int highScore = 0;
        int gamesPlayed = 0;
        int winCount = 0;
        int lossCount = 0;

        String tmpName;
        String tmpValue;

        id = playerElement.getAttribute("id");
        NodeList nList = playerElement.getChildNodes();

        for (int i = 0; i < nList.getLength(); i++) {
            tmpName = nList.item(i).getNodeName();
            tmpValue = nList.item(i).getTextContent();

            switch (tmpName) {
                case "name":
                    name = tmpValue;
                    break;
                case "gamesPlayed":
                    gamesPlayed = Integer.parseInt(tmpValue);
                    break;
                case "highScore":
                    highScore = Integer.parseInt(tmpValue);
                    break;
                case "winCount":
                    winCount = Integer.parseInt(tmpValue);
                    break;
                case "lossCount":
                    lossCount = Integer.parseInt(tmpValue);
                    break;
            }
        }

        return new Player(id, name, highScore, gamesPlayed, winCount, lossCount);
    }

    /**
     * Method used to generate a hashed Player ID based on Player's name.
     * Works by taking each byte in Player's name and appending a random number
     * to each byte. These bytes are then hashed with MD5 and transformed back into ASCII.
     * The first 5 characters are returned for brevity. Hashes can be made more collision-proof
     * by extending this length.
     *
     * @param name Player name.
     * @return (String) Hashed Player ID.
     */
    public String getID(String name) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            Formatter formatter = new Formatter();
            Random rnd = new Random();

            byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
            byte[] hash = md.digest(nameBytes);

            for (byte b : hash) {
                formatter.format("%02X", b);
                formatter.format("%02X", rnd.nextInt());
            }

            return formatter.toString().substring(0, 5);

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * Method used to sort a hashmap by value in descending order.
     * Hashmap values are put into a LinkedList. The sort() method
     * is then called, using mergesort.
     * Values are then placed into a new Hashmap and returned.
     *
     * @param hm Hashmap to be sorted.
     * @return Sorted Hashmap in descending order.
     */
    public HashMap<String, Integer> sortHashMap(HashMap<String, Integer> hm) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(hm.entrySet());
        HashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        list.sort((o1, o2) -> (o2.getValue().compareTo(o1.getValue())));

        for (Map.Entry<String, Integer> aa : list) {
            sortedMap.put(aa.getKey(), aa.getValue());
        }

        return sortedMap;
    }
}
