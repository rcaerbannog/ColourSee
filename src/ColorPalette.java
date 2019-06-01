import java.awt.*;
import javax.swing.*;
public class ColorPalette extends JPanel{
    private Color selectedColor;

    private ColorButton[][] buttons;

    public ColorPalette(int rows, int cols, Color[] buttonColors, String[] labels){
        super();

        setLayout(new GridLayout(rows, cols, 2, 2));

        //Construct a grid of ColorButtons
        //With given Color and Text
        for (int i = 0; i < buttonColors.length; i++){
            String label = (labels != null)? labels[i] : "";
            this.add(new ColorButton(label, buttonColors[i], this));
        }

        selectedColor = new Color(0, 0, 0);
    }

    public void setSelectedColor(Color c){
        selectedColor = c;
    }

    public Color getSelectedColor(){
        return selectedColor;
    }
}
