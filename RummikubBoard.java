import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
//
//// Class representing the Rummikub board
//public class RummikubBoard extends JPanel {
//    private static final int ROWS = 10;
//    private static final int COLS = 15;
//
//    public RummikubBoard() {
//        setLayout(new GridLayout(ROWS, COLS, 1, 1)); // Add a small gap between tiles
//        setPreferredSize(new Dimension(COLS * 50, ROWS * 75)); // Set preferred size
//        setBackground(Color.WHITE); // Set background color
//        createBoard(); // Create the board
//    }
//
//    private void createBoard() {
//        for (int i = 0; i < ROWS * COLS; i++) {
//            JPanel tilePanel = new JPanel(); // Create a panel for the tile
//            tilePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add a black border
//            add(tilePanel); // Add the tile panel to the board
//        }
//    }
//}

// Class representing the Rummikub board
class RummikubBoard extends JPanel {
    public RummikubBoard() {
        setBackground(new Color(139, 69, 19)); // Brown color
        setLayout(new GridLayout(10, 18, 2, 2)); // Grid layout with spaces between tiles
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 18; j++) {
                TileButton t = new TileButton("", Color.WHITE);
//                t.setBackground(Color.green);
                add(t);
            }
        }
    }
    public RummikubBoard(RummikubBoard rummikubBoard) {
        setBackground(new Color(139, 69, 19)); // Brown color
        Component[] components = rummikubBoard.getComponents();
        int n = 0;
        setLayout(new GridLayout(10, 18, 2, 2)); // Grid layout with spaces between tiles
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 18; j++) {
                if (components[n] instanceof TileButton) {
                    TileButton button = (TileButton) components[n];
                    TileButton t = new TileButton(button.getText(), button.getBackground());
//                t.setBackground(Color.green);
                    add(t);
                }
                n++;
            }
        }
    }

    public List<List<Tile1>> checkBoard() {
        Component[] components = getComponents();
        List<List<Tile1>> board = new ArrayList<>();
        List<Tile1> currentSeries = new ArrayList<>();

        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof TileButton) {
                TileButton button = (TileButton) components[i];

                // If the button color is not white, add to current series
                if (!button.getBackground().equals(Color.WHITE)) {
                    Tile1 tile = new Tile1(button.getOriginalColor(), Integer.parseInt(button.getText()));
                    currentSeries.add(tile);
                }

                // If the button color is white or we reach the end of a row, end the current series
                if (button.getBackground().equals(Color.WHITE) || (i + 1) % 18 == 0) {
                    if (!currentSeries.isEmpty()) {
                        board.add(new ArrayList<>(currentSeries));
                        currentSeries.clear();
                    }
                }
            }
        }

        // Add any remaining series to the board
        if (!currentSeries.isEmpty()) {
            board.add(currentSeries);
        }

        return board;
    }

    public void resetAndPlaceTiles(RummikubBoard temp_gameBoard, PlayerBoard playerBoard) {
        List<List<Tile1>> currentBoardList = checkBoard();
        List<List<Tile1>> previousBoardList = temp_gameBoard.checkBoard();
        List<Tile1> sideList = new ArrayList<>();

        // Flatten the lists of lists for easier comparison
        List<Tile1> currentBoardFlatList = currentBoardList.stream().flatMap(List::stream).collect(Collectors.toList());
        List<Tile1> previousBoardFlatList = previousBoardList.stream().flatMap(List::stream).collect(Collectors.toList());

        // Collect tiles that appear in current board but not in the previous board
        for (Tile1 tile : currentBoardFlatList) {
            boolean found = false;
            for (Tile1 prevTile : previousBoardFlatList) {
                if (tile.getNumber() == prevTile.getNumber() && tile.getColor().equals(prevTile.getColor())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                sideList.add(tile);
            }
        }

        // Reset current board to match the previous board
        Component[] currentComponents = getComponents();
        Component[] previousComponents = temp_gameBoard.getComponents();

        for (int i = 0; i < currentComponents.length; i++) {
            if (currentComponents[i] instanceof TileButton && previousComponents[i] instanceof TileButton) {
                TileButton currentButton = (TileButton) currentComponents[i];
                TileButton previousButton = (TileButton) previousComponents[i];

                currentButton.setText(previousButton.getText());
                currentButton.setBackground(previousButton.getBackground());
                currentButton.setOriginalColor(previousButton.getOriginalColor());
            }
        }

        // Insert the side list into the player board
        playerBoard.insertSideList(sideList);
        System.out.println("success resetAndPlaceTiles");
    }


    public List<List<Tile1>> subtractAndCheckBoard(RummikubBoard previousBoard) {
        Component[] currentComponents = getComponents();
        Component[] previousComponents = previousBoard.getComponents();
        RummikubBoard tempBoard = new RummikubBoard(this);

        // Subtract the tiles from the previous board
        for (int i = 0; i < currentComponents.length; i++) {
            if (currentComponents[i] instanceof TileButton && previousComponents[i] instanceof TileButton) {
                TileButton tempButton = (TileButton) tempBoard.getComponents()[i];
                TileButton previousButton = (TileButton) previousComponents[i];

                if (tempButton.getOriginalColor().equals(previousButton.getOriginalColor()) && tempButton.getText().equals(previousButton.getText())) {
                    tempButton.setText("");
                    tempButton.setBackground(Color.WHITE);
                    tempButton.setOriginalColor(Color.WHITE);
                }
            }
        }

        // Use the checkBoard method to get the list of lists from the temporary board
        return tempBoard.checkBoard();
    }

    public void placeTileGroups(List<List<Tile1>> tileGroups) {
        Component[] components = getComponents();
        int index = 0;

        // Reset the entire board to white background and no text
        for (Component component : components) {
            if (component instanceof TileButton) {
                TileButton button = (TileButton) component;
                button.setText("");
                button.setBackground(Color.WHITE);
                button.setOriginalColor(Color.WHITE);
            }
        }

        // Place the tiles on the board
        for (List<Tile1> group : tileGroups) {
            int groupSize = group.size();
            int currentRow = index / 18;

            // Check if the current group fits in the remaining space of the current row
            if (index % 18 + groupSize > 18) {
                // Move to the start of the next row
                currentRow++;
                index = currentRow * 18;
            }

            for (Tile1 tile : group) {
                if (index >= components.length) {
                    // If the board is full, break
                    return;
                }

                // Place the tile on the board
                if (components[index] instanceof TileButton) {
                    TileButton button = (TileButton) components[index];
                    button.setText(Integer.toString(tile.getNumber()));
                    button.setBackground(tile.getColor());
                    button.setOriginalColor(tile.getColor());
                }
                index++;
            }

            // Skip three button spaces between groups
            index += 4;

            // Check if we are still within the same row after adding space
            if (index / 18 > currentRow) {
                // Move to the start of the next row
                index = (currentRow + 1) * 18;
            }
        }
    }

    public void clearBoard(){
        Component[] components = getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof TileButton) {
                TileButton button = (TileButton) components[i];
                button.setOriginalColor(Color.WHITE);
                button.setBackground(Color.WHITE);
                button.setText("");
            }
        }
    }

}
