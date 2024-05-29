import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Class representing the player's board
//public class PlayerBoard extends JPanel {
//    private static final int ROWS = 2;
//    private static final int COLS = 15;
//
//    public PlayerBoard() {
//        setLayout(new GridLayout(ROWS, COLS, 1, 1)); // Add a small gap between tiles
//        setPreferredSize(new Dimension(COLS * 50, ROWS * 75)); // Set preferred size
//        setBackground(Color.GREEN); // Set background color
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
// Class representing the player's board
class PlayerBoard extends JPanel {
    public PlayerBoard() {
        setBackground(new Color(139, 69, 19)); // Brown color
        setLayout(new GridLayout(3, 15, 4, 4)); // Grid layout with spaces between tiles
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 15; j++) {
                add(new TileButton("", Color.WHITE));
            }
        }
    }
    public List<Tile1> updateButtons(List<Tile1> tiles) {
        Component[] components = getComponents();
        List<Tile1> list = new ArrayList<>();
        int i = 0;
        for (Component component : components) {
            if (component instanceof TileButton) {
                TileButton button = (TileButton) component;
                button.setText(Integer.toString(tiles.get(0).getNumber()));
                button.setBackground(tiles.get(0).getColor());
                button.setOriginalColor(tiles.get(0).getColor());
                list.add(tiles.get(0));
                tiles.remove(0);
                i++;
            }
            if(i>14)
                break;
        }
        return list;
    }
    public void player_takeButton(List<Tile1> tiles) {
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof TileButton) {
                TileButton button = (TileButton) component;
                if(button.getBackground() == Color.WHITE){
                    button.setText(Integer.toString(tiles.get(0).getNumber()));
                    button.setBackground(tiles.get(0).getColor());
                    button.setOriginalColor(tiles.get(0).getColor());
                    tiles.remove(0);
                    break;
                }
            }
        }
    }

    public void insertSideList(List<Tile1> sideList) {
        Component[] components = getComponents();
        Iterator<Tile1> iterator = sideList.iterator();

        for (Component component : components) {
            if (iterator.hasNext() && component instanceof TileButton) {
                TileButton button = (TileButton) component;
                if (button.getBackground().equals(Color.WHITE)) {
                    Tile1 tile = iterator.next();
                    button.setText(Integer.toString(tile.getNumber()));
                    button.setBackground(tile.getColor());
                    button.setOriginalColor(tile.getColor());
                }
            }
        }
        System.out.println("success insertSideList");

    }


}
