import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.awt.Color;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.util.*;
import static java.awt.Color.*;
public class RummikubGUI extends JFrame implements MouseListener {
    private RummikubBoard gameBoard;
    private RummikubBoard temp_gameBoard;
    private PlayerBoard playerBoard;
    private PlayerBoard temp_playerBoard;

    private ButtonsBoard buttonBoard;

    private JPanel buttonPanel;
    private JButton undoButton;
    private JButton takeTileButton;
    private JButton finishButton;
    private List<List<Tile1>> gameBoardList;
    private List<Tile1> botListTiles;

    private boolean player_turn = true;
    private boolean player_first_done = false;
    private boolean bot_first_done = false;



    // GameLogic gameLogic;

    /*
    implements mouse listener
    creates the GameLogic object and pass the pointer on this
     */

    public RummikubGUI()  {

        // gameLogic = new GameLogic(this);

        setSize(1100,1000);
        setTitle("Rummikub");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        // Create the game board
        gameBoard = new RummikubBoard();
        gameBoard.setBounds(50,20, 960, 700);
        add(gameBoard);

        temp_gameBoard = new RummikubBoard(gameBoard);
        gameBoardList = new ArrayList<>();

        // Create the player board
        playerBoard = new PlayerBoard();
        playerBoard.setBounds(10,730, 800, 210);

        List<Tile1> pile = new ArrayList<>(PileTiles());
        playerBoard.updateButtons(pile);
        add(playerBoard);

        botListTiles = new ArrayList<>(pile.subList(0, 14));
        for (int i = 0; i < 14; i++) {
            pile.remove(0);
        }
        System.out.println(botListTiles);

        buttonBoard = new ButtonsBoard();
        buttonBoard.setBounds(850,750, 225, 200);

        buttonBoard.getUndoButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoFunction(gameBoard, temp_gameBoard, playerBoard);
            }
        });

        buttonBoard.getTakeTileButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoFunction(gameBoard, temp_gameBoard, playerBoard);
                playerBoard.player_takeButton(pile);
                activateBot(botListTiles, gameBoardList, bot_first_done, gameBoard, temp_gameBoard, player_turn, pile);
            }
        });

        buttonBoard.getFinishButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean flag = true;
                List<List<Tile1>> board;
                if (player_turn && !player_first_done) {
                    board = gameBoard.subtractAndCheckBoard(temp_gameBoard);
                    if (isFirstDone(board)) {
                        player_first_done = true;
                    }
                }
                board = gameBoard.checkBoard();

                if (countTiles(board) <= countTiles(temp_gameBoard.checkBoard())){
                    System.out.println("you need to put more tiles than you take or take a tile from the pile");
                }
                else if (player_turn && player_first_done) {
                    for (List<Tile1> g : board) {
                        if (!Bot.isValidGroup(g)) {
                            System.out.println("you should take a tile");
                            flag = false;
                            break;
                        }
                        if (!flag)
                            break;
                    }
                    if (flag) {
                        System.out.println("valid");
                        gameBoardList = board;
                        temp_gameBoard = new RummikubBoard(gameBoard);
                        System.out.println("success");
                        player_turn = false;
                        activateBot(botListTiles, gameBoardList, bot_first_done, gameBoard, temp_gameBoard,player_turn, pile);
                        System.out.println("player_turn: " + player_turn);
                    }

                }
                else
                    System.out.println("you need 30 points series");
            }
        });
        add(buttonBoard);

        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    public List<Tile1> PileTiles(){
        List<Tile1> tiles = new ArrayList<>();
        Color[] colors = { Color.BLACK, Color.BLUE, RED, Color.YELLOW };

        // Add instances of numbers 1-13 in all colors
        for (Color color : colors) {
            for (int number = 1; number <= 13; number++) {
                tiles.add(new Tile1(color, number));
                tiles.add(new Tile1(color, number));
            }
        }


        // Shuffle the tiles
        Collections.shuffle(tiles);

        // Print the tiles
//        for (Tile1 tile : tiles) {
//            System.out.println(tile);
//        }
        return tiles;
    }

    public boolean isFirstDone(List<List<Tile1>> list) {
        int sum = 0;

        // Iterate through each list in the list of lists
        for (List<Tile1> tileList : list) {
            // Iterate through each Tile1 in the current list
            for (Tile1 tile : tileList) {
                // Add the number of the tile to the sum
                sum += tile.getNumber();
            }
        }

        // Return true if the sum is greater than or equal to 30, otherwise false
        return sum >= 30;
    }

    public void activateBot(List<Tile1> botListTiles, List<List<Tile1>> gameBoardList,
                            boolean bot_first_done, RummikubBoard gameBoard, RummikubBoard temp_gameBoard,
                            boolean player_turn, List<Tile1> pile){
        List<Tile1> find = new ArrayList<>(this.botListTiles);
        System.out.println("bot's list: " + find);
        this.gameBoardList = this.gameBoard.checkBoard();
        System.out.println("gameBoardList: " + countTiles(this.gameBoardList));
        if (this.bot_first_done){
            for (List<Tile1> g:this.gameBoardList){
                find.addAll(g);
            }
        }

        List<Tile1> possible_tiles = new ArrayList<>();
        System.out.println("find after board: " + find);
        List<List<Tile1>> validGroups = Bot.findValidGroups(find, possible_tiles);
        System.out.println("leftOvers for bot: " + possible_tiles);
        System.out.println("compare to bot's list: " + this.botListTiles);

        // Print the valid groups
        int groupNumber = 1;
        for (List<Tile1> group : validGroups) {
            System.out.println("Group " + groupNumber + ": " + group);
            groupNumber++;
        }

        System.out.println("compare gameBoardList to validGroups");
        System.out.println("gameBoardList: " + countTiles(this.gameBoardList));
        System.out.println("validGroups: " + countTiles(validGroups));
        if (!bot_first_done){
            if (isFirstDone(validGroups)){
                this.bot_first_done = true;
                System.out.println("bot_first_done: " + this.bot_first_done);
                validGroups.addAll(this.gameBoardList);
                this.gameBoard.placeTileGroups(validGroups);
                this.botListTiles = possible_tiles;
                this.temp_gameBoard = new RummikubBoard(this.gameBoard);
            }
            else{
                botListTiles.add(pile.get(0));
                pile.remove(0);
            }
        }
        else if (bot_first_done && countTiles(validGroups)>countTiles(this.gameBoardList)){
            this.gameBoard.clearBoard();
            this.gameBoard.placeTileGroups(validGroups);
            this.botListTiles = possible_tiles;
            this.temp_gameBoard = new RummikubBoard(gameBoard);
        }
        else{
            botListTiles.add(pile.get(0));
            pile.remove(0);
        }
        this.player_turn = true;
    }

    public int countTiles(List<List<Tile1>> list){
        int sum = 0;

        // Iterate through each list in the list of lists
        for (List<Tile1> tileList : list) {
            // Iterate through each Tile1 in the current list
            for (Tile1 tile : tileList) {
                // Add the number of the tile to the sum
                sum++;
            }
        }
        return sum;
    }

    public void undoFunction(RummikubBoard gameBoard, RummikubBoard temp_gameBoard, PlayerBoard playerBoard){
        System.out.println(gameBoard.checkBoard());
        System.out.println(temp_gameBoard.checkBoard());
        gameBoard.resetAndPlaceTiles(temp_gameBoard, playerBoard);
        System.out.println("after");
        System.out.println(gameBoard.checkBoard());
        System.out.println(temp_gameBoard.checkBoard());
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
    @Override
    public void mousePressed(MouseEvent e) {


        /*

         */

//        for (int i = 0; i < gameBoard.get)
//        if (e.getSource() ==  gameBoard.get[i])
//
//            if (e.getX())
//
//                turn

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
}