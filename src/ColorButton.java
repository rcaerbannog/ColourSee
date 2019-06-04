import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
public class ColorButton extends Button implements MouseListener{
    private Color buttonColor;
    private Color appearColor;
    private ColorPalette parentPalette;

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

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     */
    @Override
    public void mouseClicked(MouseEvent e){
        parentPalette.setSelectedColor(buttonColor);
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
