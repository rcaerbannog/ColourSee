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

    /** The last selected button */
    private ColorButton selectedButton;

    /** The array of ColorButtons in the palette */
    private ColorButton[] buttons;

    /** Shows the current selected color */
    private JButton colorSwatch;

    /** Whether the color filter is applied or not */
    private boolean isColorFilterApplied;

    /**
     * Class constructor: creates the palette
     * Appearance Color will be the same as button Color
     * @param rows The number of rows in the palette
     * @param cols The number of columns in the palette
     * @param buttonColors The Colors to create a button for
     * @param labels The text on each button
     */
    public ColorPalette(int rows, int cols, Color[] buttonColors, String[] labels){
        this(rows, cols, buttonColors, buttonColors, labels);
    }

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
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(rows, cols, 2, 2));

        buttons = new ColorButton[buttonColors.length];
        //Construct a grid of ColorButtons
        //With given Color and Text
        for (int i = 0; i < buttonColors.length; i++){
            String label = (labels != null)? labels[i] : "";
            buttons[i] = (new ColorButton(label, buttonColors[i], appearColors[i],this));
            buttonPanel.add(buttons[i]);
        }
        selectedColor = buttonColors[0];
        selectedButton = buttons[0];

        //Construct the color swatch
        colorSwatch = new JButton();
        Dimension swatchD = new Dimension(40, 40);
        colorSwatch.setPreferredSize(swatchD);
        colorSwatch.setMinimumSize(swatchD);
        colorSwatch.setMaximumSize(swatchD);
        colorSwatch.setAlignmentY(JButton.CENTER_ALIGNMENT);

        //Add everything to the main panel
        this.add(buttonPanel);
        this.add(Box.createRigidArea(new Dimension(20, 0)));
        this.add(colorSwatch);

        //Misc
        isColorFilterApplied = false;
        buttonPanel.setMinimumSize(buttonPanel.getPreferredSize());
        buttonPanel.setMaximumSize(buttonPanel.getPreferredSize());
    }

    /**
     * Sets the selected ColorButton and Color
     * @param cb The new selected ColorButton
     */
    public void setSelectedColorButton(ColorButton cb){
        selectedButton = cb;
        selectedColor = cb.getButtonColor();
        if (!isColorFilterApplied)
            colorSwatch.setBackground(selectedColor);
        else{
            colorSwatch.setBackground(cb.getAppearColor());
        }
        this.repaint();
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
        isColorFilterApplied = true;
        setSelectedColorButton(selectedButton);
    }

    public void changeButtonsToButtonColor(){
        for (ColorButton b: buttons){
            b.setBackground(b.getButtonColor());
        }
        isColorFilterApplied = false;
        setSelectedColorButton(selectedButton);
    }
}
