import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonsBoard extends JPanel {
    private  JButton undoButton;
    private JButton takeTileButton;
    private JButton finishButton;
    public ButtonsBoard() {
        setBackground(Color.DARK_GRAY); // Brown color
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Use BoxLayout with Y_AXIS alignment

        undoButton = new JButton("Undo");
        takeTileButton = new JButton("Take a Tile");
        finishButton = new JButton("Finish");

        // Set alignment for buttons to center
        undoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        takeTileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        finishButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        undoButton.setSize(100,50);
        takeTileButton.setSize(100,50);
        finishButton.setSize(100,50);



        add(Box.createVerticalStrut(30)); // Add space of 10 pixels between buttons
        add(undoButton);
        add(Box.createVerticalStrut(30)); // Add space of 10 pixels between buttons
        add(takeTileButton);
        add(Box.createVerticalStrut(30)); // Add space of 10 pixels between buttons
        add(finishButton);
    }
    public JButton getUndoButton() {
        return undoButton;
    }

    public void setUndoButton(JButton undoButton) {
        this.undoButton = undoButton;
    }

    public JButton getTakeTileButton() {
        return takeTileButton;
    }

    public void setTakeTileButton(JButton takeTileButton) {
        this.takeTileButton = takeTileButton;
    }

    public JButton getFinishButton() {
        return finishButton;
    }

    public void setFinishButton(JButton finishButton) {
        this.finishButton = finishButton;
    }
}
