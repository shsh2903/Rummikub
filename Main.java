
import java.awt.Color;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.util.*;
import static java.awt.Color.*;

public class Main {
    public static void main(String[] args) {

//        // Create a list of tiles
//        List<Tile1> tiles = new ArrayList<>();
//        //tiles.add(new Tile1(RED, 1));
//        tiles.add(new Tile1(YELLOW, 1));
//        tiles.add(new Tile1(BLUE, 1));
//        tiles.add(new Tile1(BLUE, 1));
//        tiles.add(new Tile1(BLUE, 2));
//        tiles.add(new Tile1(BLUE, 2));
//        tiles.add(new Tile1(BLUE, 3));
//        tiles.add(new Tile1(BLUE, 3));
//        tiles.add(new Tile1(BLUE, 4));
//        tiles.add(new Tile1(BLUE, 6));
//        tiles.add(new Tile1(BLUE, 6));
//        tiles.add(new Tile1(BLUE, 7));
//        tiles.add(new Tile1(BLUE, 7));
//        tiles.add(new Tile1(BLUE, 8));
//        tiles.add(new Tile1(BLUE, 8));
//        tiles.add(new Tile1(BLUE, 9));
//        tiles.add(new Tile1(RED, 3));
//        tiles.add(new Tile1(RED, 4));
//        tiles.add(new Tile1(RED, 5));
//        tiles.add(new Tile1(RED, 3));
//        tiles.add(new Tile1(RED, 4));
//        tiles.add(new Tile1(RED, 5));
//        tiles.add(new Tile1(BLACK, 8));
//        tiles.add(new Tile1(BLACK, 9));
//        tiles.add(new Tile1(BLACK, 10));
//        tiles.add(new Tile1(YELLOW, 8));
//        tiles.add(new Tile1(YELLOW, 9));
//        tiles.add(new Tile1(RED, 8));
//        tiles.add(new Tile1(RED, 9));
//        tiles.add(new Tile1(YELLOW, 10));
//        tiles.add(new Tile1(YELLOW, 11));
//        tiles.add(new Tile1(BLUE, 10)); // Additional tile
//        tiles.add(new Tile1(RED, 10)); // Additional tile
//        tiles.add(new Tile1(Color.YELLOW, 10)); // Additional tile
//        tiles.add(new Tile1(BLUE, 10)); // Additional tile
//        tiles.add(new Tile1(RED, 10)); // Additional tile
//        //tiles.add(new Tile1(Color.YELLOW, 10)); // Additional tile
//
//        // Get valid groups of tiles
//        List<List<Tile1>> validGroups = findValidGroups(tiles);
//
//        // Print the valid groups
//        int groupNumber = 1;
//        for (List<Tile1> group : validGroups) {
//            System.out.println("Group " + groupNumber + ": " + group);
//            groupNumber++;
//        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create an instance of RummikubGUI with the desired board sizes
                RummikubGUI rummikubGUI = new RummikubGUI();
                // Display the GUI
                rummikubGUI.setVisible(true);
            }
        });

    }

    // Find valid groups of tiles

}


