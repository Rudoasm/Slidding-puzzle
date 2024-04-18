

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    static char[][] map;
    static Node startNode;
    static Node finishtNode;

    public static void main(String[] args) {
        map = mapReader("Mazes/maze10_3.txt");
        printMap(map);
    }

    // Reading a map from an input text file----------------------------------------------------------------------------
    public static char[][] mapReader(String filePath) {
        char[][] map = null;
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(filePath));

            // Getting the dimensions of the map-------------------------------------
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
            //-----------------------------------------------------------------------

            // Writing values into the map-------------------------------------------
            fileReader = new BufferedReader(new FileReader(filePath));
            for (int y = 0; y < rows; y++) {
                line = fileReader.readLine();
                for (int x = 0; x < cols; x++) {
                    char character = line.charAt(x);
                    map[y][x] = character;

                    // Getting the starting and finishing positions---
                    if (character == 'S') {
                        startNode = new Node(x+1, y+1);
                    } else if (character == 'F') {
                        finishtNode = new Node(x+1, y+1);
                    }
                    //------------------------------------------------
                }
            }
            fileReader.close();
            //-----------------------------------------------------------------------

        } catch (FileNotFoundException e) {
            System.out.println("The File you are looking for does not exist.");
        } catch (IOException e) {
            System.out.println("There was an error while reading from the File.");
        }

        return map;
    }
    //------------------------------------------------------------------------------------------------------------------

    // Printing a map represented using a 2D array----------------------------------------------------------------------
    public static void printMap(char[][] map) {
        for (char[] charRow : map) {
            for (char character : charRow) {
                System.out.print(character);
            }
            System.out.println();
        }
    }
    //------------------------------------------------------------------------------------------------------------------
}