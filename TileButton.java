import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Class representing the Rummikub tile
class TileButton extends JButton implements ActionListener {
    private static TileButton lastClickedButton = null; // Keep track of the last clicked button

    private Color originalColor; // Store the original color of the button

    private boolean clicked;

    public TileButton(String text, Color backgroundColor) {
        super(text);
        clicked = false;
        setOpaque(true);

        setBackground(backgroundColor);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setPreferredSize(new Dimension(50, 50));
        originalColor = backgroundColor; // Store the original color
        Font font = new Font("Arial", Font.BOLD, 16); // Example font with size 16 and bold style
        setFont(font);
        setForeground(Color.WHITE);

        addActionListener(this); // Add action listener to handle button clicks
    }

    // Implement actionPerformed method to handle button clicks
    @Override
    public void actionPerformed(ActionEvent e) {

        if(lastClickedButton == null){
            setBackground(Color.green); // Change background color to orange
            this.clicked = true;
            lastClickedButton = this; // Update the last clicked button
        }
        else if (lastClickedButton != null && lastClickedButton != this && lastClickedButton instanceof TileButton
        && lastClickedButton.clicked ) {
            if (getBackground() == Color.WHITE){
                setBackground(lastClickedButton.originalColor);
                setOriginalColor(lastClickedButton.originalColor);
                setText(lastClickedButton.getText());
                lastClickedButton.setOriginalColor(Color.WHITE);
                lastClickedButton.setBackground(Color.WHITE); // Reset the background color of the previous button
                lastClickedButton.setText(""); // Reset the background color of the previous button
                lastClickedButton.setClicked(false);
                lastClickedButton = this; // Update the last clicked button
            }
            else
                System.out.println("you cant put a tile here");
        }
        else{
            setBackground(Color.green); // Change background color to orange
            this.clicked = true;
            lastClickedButton = this; // Update the last clicked button
        }



    }
    public static TileButton getLastClickedButton() {
        return lastClickedButton;
    }

    public static void setLastClickedButton(TileButton lastClickedButton) {
        TileButton.lastClickedButton = lastClickedButton;
    }

    public Color getOriginalColor() {
        return originalColor;
    }

    public void setOriginalColor(Color originalColor) {
        this.originalColor = originalColor;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}

