import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

/**
 * Button that has background colour and automatically constructed ActionListener
 * to change the selected Color of its using ColorPalette
 *
 * @author Alexander Li
 * @version 2019-06-05 v1.0
 */
public class ColorButton extends Button implements MouseListener{

    /** The color that the selected color of the palette will be changed to when this is clicked */
    private Color buttonColor;
    /** The background color of this button: color blindness filter is applied */
    private Color appearColor;
    /** The ColorPalette that this ColorButton belongs to */
    private ColorPalette parentPalette;

    /**
     * Class constructor
     * @param label The text (should be letter or number) on the button
     * @param buttonColor The select Color of the button
     * @param appearColor The background Color of the button
     * @param parentPalette The ColorPalette that this button belongs to
     */
    public ColorButton(String label, Color buttonColor, Color appearColor, ColorPalette parentPalette){
        super(label);

        this.buttonColor = buttonColor;
        this.appearColor = appearColor;
        this.parentPalette = parentPalette;

        setBackground(appearColor);
        setPreferredSize(new Dimension(20, 20));
        setMaximumSize(new Dimension(20, 20));
        addMouseListener(this);

    }

    public Color getAppearColor() {
        return appearColor;
    }

    public Color getButtonColor() {
        return buttonColor;
    }

    /**
     * Invoked when the mouse button has been clicked (pressed and released) on a component.
     * Changes the selected Color of the palette, which determines the fill Color
     */
    @Override
    public void mouseClicked(MouseEvent e){
        parentPalette.setSelectedColorButton(this);
        parentPalette.repaint();
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    @Override
    public void mousePressed(MouseEvent e){}

    /**
     * Invoked when a mouse button has been released on a component.
     */
    @Override
    public void mouseReleased(MouseEvent e){}

    /**
     * Invoked when the mouse enters a component.
     */
    @Override
    public void mouseEntered(MouseEvent e){}

    /**
     * Invoked when the mouse exits a component.
     */
    @Override
    public void mouseExited(MouseEvent e){}
}
