import java.awt.*;
import javax.swing.*;
public class ColorPalette extends JPanel{
    private Color selectedColor;

    private ColorButton[][] buttons;

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

    public void setSelectedColor(Color c){
        selectedColor = c;
    }

    public Color getSelectedColor(){
        return selectedColor;
    }
}
