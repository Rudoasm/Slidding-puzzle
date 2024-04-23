import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {

    static char[][] map;
    static Node startNode;
    static Node finishNode;

    public static void main(String[] args) {
        map = mapReader("C:\\algo\\ASMaiman_20220055\\BM\\benchmark_series\\puzzle_10.txt");
        // Change the map from here
        displayMap(map);

        // Start Time
        long startTime = System.currentTimeMillis();

        List<Node> shortestPath = findShortestPathBetweenNodes();
        if (shortestPath != null) {
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("Shortest path found. See below 🔽");
            System.out.println("");
            List<String> detailedSteps = getDetailedSteps(shortestPath);
            for (int i = 0; i < detailedSteps.size(); i++) {
                System.out.println((i + 1) + ". " + detailedSteps.get(i));
            }
        } else {
            System.out.println("No path found.");
        }
        long endTime = System.currentTimeMillis();

        // Run Time
        long runTime = endTime - startTime;
        System.out.println("\nTime taken for the algorithm to come across the shortest path: " + runTime + " milliseconds.");
    }

    // Reading a map from an input text file
    public static char[][] mapReader(String filePath) {
        char[][] map = null;
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(filePath));

            // Getting the dimensions of the map
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

            // Filling the maps with values
            fileReader = new BufferedReader(new FileReader(filePath));
            for (int y = 0; y < rows; y++) {
                line = fileReader.readLine();
                for (int x = 0; x < cols; x++) {
                    char character = line.charAt(x);
                    map[y][x] = character;
                }
            }
            fileReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("The File does not exist.");
        } catch (IOException e) {
            System.out.println("An Error occurred while reading the file.");
        }

        return map;
    }

    // Finding the shortest path between start and end positions
    public static List<Node> findShortestPathBetweenNodes() {

        // Creating Nodes for every valid position and set their distances
        int[][] distances = new int[map.length][map[0].length];
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == 'S') {
                    startNode = new Node(x, y);
                    distances[y][x] = 0;
                } else if (map[y][x] != '0') {
                    distances[y][x] = Integer.MAX_VALUE;
                }
            }
        }

        // Creating a Priority Queue (Min-Heap) for storing Nodes for later visitations.
        MinHeap<Node> queue = new MinHeap<>(1000, Comparator.comparingInt(node -> distances[node.getY()][node.getX()]));
        queue.add(startNode);

        // Dijkstra Algorithm
        while (!queue.isEmpty()) {
            // Getting the Node with the shortest distance and removing it from the queue
            Node currentNode = queue.poll();

            // Checking if the Current Node is the Finish Node
            if (map[currentNode.getY()][currentNode.getX()] == 'F') {
                finishNode = currentNode;
                break;
            }

            // Exploring the next possible Adjacent Nodes
            checkNextNodes(currentNode, distances, queue);
        }

        // Path created
        List<Node> shortestPath = new ArrayList<>();
        Node currentNode = finishNode;
        while (currentNode != null) {
            shortestPath.add(currentNode);
            currentNode = currentNode.getPrev();
        }

        return shortestPath.isEmpty() ? null : shortestPath;
    }

    // To Check if a position is within the boundaries of the map or not.
    private static boolean locationIsValid(int x, int y) {
        return x >= 0 && x < map[0].length && y >= 0 && y < map.length;
    }

    // To get the detailed precise steps like move left/right/up/down from the current position.
    private static List<String> getDetailedSteps(List<Node> shortestPath) {
        List<String> detailedSteps = new ArrayList<>();
        Node start = shortestPath.get(shortestPath.size() - 1);
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

    // Exploring the available Adjacent Nodes
    private static void checkNextNodes(Node currentNode, int[][] distances, MinHeap<Node> queue) {
        // A 2D array that stores all the possible directions that can be traveled from the current coordinate/position (up, right, left, down)
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        // Moving to all directions one block at a time
        for (int[] direction : directions) {
            int newX = currentNode.getX() + direction[0];
            int newY = currentNode.getY() + direction[1];
            int distanceTraveled = 0;

            // Keep moving until a rock is hit or at the end of the border of the map
            while (locationIsValid(newX, newY) && map[newY][newX] != '0') {
                if (map[newY][newX] == 'F') {
                    // Moving to the next block in the same direction
                    newX += direction[0];
                    newY += direction[1];
                    distanceTraveled++;
                    break;
                } else {
                    newX += direction[0];
                    newY += direction[1];
                    distanceTraveled++;
                }
            }

            // Adding the last valid position just before hitting a boulder or "0"
            newX -= direction[0];
            newY -= direction[1];
            int newDistance = distances[currentNode.getY()][currentNode.getX()] + distanceTraveled;

            if (newDistance < distances[newY][newX]) {
                distances[newY][newX] = newDistance;
                Node nextNode = new Node(newX, newY);
                nextNode.setPrev(currentNode);
                queue.add(nextNode);
            }
        }
    }

    // Printing a map through a 2D array
    public static void displayMap(char[][] map) {
        for (char[] charRow : map) {
            for (char character : charRow) {
                System.out.print(character + " ");
            }
            System.out.println();
        }
    }
}
