import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    static char[][] map;
    static Node startNode;
    static Node finishNode;

    public static void main(String[] args) {
        map = mapReader("Mazes/maze30_5.txt");
        printMap(map);

        List<Node> shortestPath = findShortestPath();
        if (shortestPath != null) {
            System.out.println("Shortest path found:");
            List<String> detailedSteps = getDetailedSteps(shortestPath);
            for (int i = 0; i < detailedSteps.size(); i++) {
                System.out.println((i + 1) + ". " + detailedSteps.get(i));
            }
        } else {
            System.out.println("No path found.");
        }
    }

    private static List<String> getDetailedSteps(List<Node> shortestPath) {
        List<String> detailedSteps = new ArrayList<>();
        Node start = shortestPath.get(shortestPath.size() - 1);
        Node end = shortestPath.get(0);
        detailedSteps.add("Start at (" + (start.getX() + 1) + ", " + (start.getY() + 1) + ")");

        for (int i = shortestPath.size() - 1; i > 0; i--) {
            Node current = shortestPath.get(i);
            Node next = shortestPath.get(i - 1);
            if (next.getX() > current.getX()) {
                detailedSteps.add("Move right to (" + (next.getX() + 1) + ", " + (next.getY() + 1) + ")");
            } else if (next.getX() < current.getX()) {
                detailedSteps.add("Move left to (" + (next.getX() + 1) + ", " + (next.getY() + 1) + ")");
            } else if (next.getY() > current.getY()) {
                detailedSteps.add("Move down to (" + (next.getX() + 1) + ", " + (next.getY() + 1) + ")");
            } else if (next.getY() < current.getY()) {
                detailedSteps.add("Move up to (" + (next.getX() + 1) + ", " + (next.getY() + 1) + ")");
            }
        }

        detailedSteps.add("Done!");
        return detailedSteps;
    }


    // Reading a map from an input text file----------------------------------------------------------------------------
    public static char[][] mapReader(String filePath) {
        char[][] map = null;
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(filePath));

            //Getting the dimensions of the map-------------------------------------
            int rows = 0;
            int cols = 0;
            while (fileReader.readLine() != null) {
                rows++;
            }
            fileReader.close();

            fileReader = new BufferedReader(new FileReader(filePath));
            String line = fileReader.readLine();
            if (line != null) {
                cols = line.length();
            }
            fileReader.close();

            map = new char[rows][cols];
            //----------------------------------------------------------------------

            //Writing values into the map-------------------------------------------
            fileReader = new BufferedReader(new FileReader(filePath));
            for (int y = 0; y < rows; y++) {
                line = fileReader.readLine();
                for (int x = 0; x < cols; x++) {
                    char character = line.charAt(x);
                    map[y][x] = character;
                }
            }
            fileReader.close();
            //----------------------------------------------------------------------

        } catch (FileNotFoundException e) {
            System.out.println("The File does not exist.");
        } catch (IOException e) {
            System.out.println("An Error occured while reading the file.");
        }

        return map;
    }
    //------------------------------------------------------------------------------------------------------------------

    // Printing a map through a 2D array----------------------------------------------------------------------
    public static void printMap(char[][] map) {
        for (char[] charRow : map) {
            for (char character : charRow) {
                System.out.print(character);
            }
            System.out.println();
        }
    }
    //------------------------------------------------------------------------------------------------------------------

    // Finding the shortest path between start and end positions--------------------------------------------------------
    public static List<Node> findShortestPath () {

        //Creating Nodes for every valid positions and set their distances----------------------------------------

        HashMap<Node, Integer> distances = new HashMap<>();
        for (int y = 0; y < map.length; y++){
            for (int x = 0; x < map[0].length; x++){
                Node node = new Node(x, y);
                if (map[y][x] == 'S') {
                    startNode = node;
                    distances.put(node, 0);
                } else if (map[y][x] != '0') {
                    distances.put(node, Integer.MAX_VALUE);
//                    note: traditionally we assign infinity at the beginning and later update the distances in da.
                }
            }
        }
        //--------------------------------------------------------------------

        //Creating a Priority Queue (Min-Heap) for storing Nodes to visit later------------------
        /*
        "Comparator.comparingInt(distances::get)" ensures that Nodes are arranged in the ascending order of the
        distance from the starting position
        */
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        queue.add(startNode);
        //---------------------------------------------------------------------------------------

        // Dijkstra Algorithm-------------------------------------

        while (!queue.isEmpty()) {
            //Getting the Node with the shortest distance and removing it from the queue. "poll()"
            Node currentNode = queue.poll();

            //Checking if the Current Node is the Finish Node--------
            if (map[currentNode.getY()][currentNode.getX()] == 'F') {
                finishNode = currentNode;
                break;
            }
            //-------------------------------------------------------

            //Exploring the next possible Adjacent Nodes-------------
            checkNextNodes(currentNode, distances, queue);

        }


        //Creating the path---------------------------------------------------
        List<Node> shortestPath = new ArrayList<>();
        Node currentNode = finishNode;
        while (currentNode != null) {
            shortestPath.add(currentNode);
            currentNode = currentNode.getPrev();
        }

        //--------------------------------------------------------------------

        return shortestPath.isEmpty() ? null : shortestPath;
    }

    // Exploring the next possible Adjacent Nodes-----------------------------------------------------------------------
    private static void checkNextNodes(Node currentNode, HashMap<Node, Integer> distances, PriorityQueue<Node> queue) {
        //A 2D array that stores all the possible directions (R, L, D, U) to be travelled from the current position
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        //Moving to all directions one block at a time------------------------
        for (int[] direction : directions) {
            int newX = currentNode.getX() + direction[0];
            int newY = currentNode.getY() + direction[1];
            int distanceTraveled = 0;

            //Keep moving until a rock is hit or at the end of the border of the map----
            while (locationIsValid(newX, newY) && map[newY][newX] != '0') {
                if (map[newY][newX] == 'F') {
                    //Moving to the next block of the same direction
                    newX += direction[0];
                    newY += direction[1];
                    distanceTraveled++;
                    break;
                } else{
                    newX += direction[0];
                    newY += direction[1];
                    distanceTraveled++;
                }
                //--------------------------------------------------------------------------
            }
            //Adding the last valid position just before hitting a boulder------------------------
            newX -= direction[0];
            newY -= direction[1];
            int newDistance = distances.get(currentNode) + distanceTraveled;

            Node adjacentNode = null;
            //Finding the node with the same coordinates if it exists in the distances map
            for (Map.Entry<Node, Integer> entry : distances.entrySet()) {
                Node node = entry.getKey();
                if (node.getX() == newX && node.getY() == newY) {
                    adjacentNode = node;
                    break;
                }
            }

            if (adjacentNode != null && newDistance < distances.get(adjacentNode)) {
                distances.put(adjacentNode, newDistance);
                adjacentNode.setPrev(currentNode);
                queue.add(adjacentNode);
            }
            //-------------------------------------------------------------------------------------------
        }
    }
    //------------------------------------------------------------------------------------------------------------------

    // Checking if a position is within the boundaries of the map-------------------------------------------------------
    private static boolean locationIsValid(int x, int y) {
        return x >= 0 && x < map[0].length && y >= 0 && y < map.length;
    }
    //------------------------------------------------------------------------------------------------------------------
}