import java.awt.*;
import javax.swing.*;

/**
 * A JPanel with a grid layout containing a grid of ColorButtons
 * Stores the buttonColor of the last ColorButton selected by the player
 *
 * @author Alexander Li
 * @version 2019-06-05 v1.0
 */
public class ColorPalette extends JPanel{
    /** The last selected Color */
    private Color selectedColor;

    /** The array of ColorButtons in the palette */
    private ColorButton[] buttons;

    /** The lens button */
    private JButton lensButton;

    /** The grid of color buttons */
    private JPanel colorGrid;


    private JButton colorSwatch;


    /**
     * Class constructor: creates the palette
     * @param rows The number of rows in the palette
     * @param cols The number of columns in the palette
     * @param buttonColors The Colors to create a button for
     * @param appearColors The corresponding appearance colour for each button
     * @param labels The text on each button
     */
    public ColorPalette(int rows, int cols, Color[] buttonColors, Color[] appearColors, String[] labels){
        super();

        setLayout(new GridLayout(rows, cols, 2, 2));

        buttons = new ColorButton[buttonColors.length];
        //Construct a grid of ColorButtons
        //With given Color and Text
        for (int i = 0; i < buttonColors.length; i++){
            String label = (labels != null)? labels[i] : "";
            buttons[i] = (new ColorButton(label, buttonColors[i], appearColors[i],this));
            this.add(buttons[i]);
        }
        selectedColor = buttonColors[0];

        //Construct the color swatch


        //Construct the lens button



        //Add everything to the main panel
    }

    /**
     * Sets the selected Color
     * @param c The new selected Color
     */
    public void setSelectedColor(Color c){
        selectedColor = c;
    }

    /**
     * Returns the last selected Color
     * @return the last selected Color
     */
    public Color getSelectedColor(){
        return selectedColor;
    }

    public void changeButtonsToAppearColor(){
        for (ColorButton b: buttons){
            b.setBackground(b.getAppearColor());
        }
    }

    public void changeButtonsToButtonColor(){
        for (ColorButton b: buttons){
            b.setBackground(b.getButtonColor());
        }
    }

    public void setLensButtonActive(boolean flag){
        if (flag)
            lensButton.setEnabled(true);
        else
            lensButton.setEnabled(false);
    }
}
