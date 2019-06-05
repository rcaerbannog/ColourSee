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
    private ColorButton[][] buttons;

    /**
     * Class constructor: creates the palette
     * @param rows The number of rows in the palette
     * @param cols The number of columns in the palette
     * @param buttonColors The Colors to create a button for
     * @param hiddenColors The corresponding background colour for each button
     * @param labels The text on each button
     */
    public ColorPalette(int rows, int cols, Color[] buttonColors, Color[] hiddenColors, String[] labels){
        super();

        setLayout(new GridLayout(rows, cols, 2, 2));

        //Construct a grid of ColorButtons
        //With given Color and Text
        for (int i = 0; i < buttonColors.length; i++){
            String label = (labels != null)? labels[i] : "";
            this.add(new ColorButton(label, buttonColors[i], hiddenColors[i],this));
        }

        selectedColor = buttonColors[0];
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
}
