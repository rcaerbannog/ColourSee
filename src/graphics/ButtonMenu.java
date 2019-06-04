package graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.*;

public class ButtonMenu extends JPanel implements ActionListener {
    public static final int AUTOSIZE = 1;

    public static final int FLOW_LAYOUT = 1;
    public static final int GRID_LAYOUT = 2;
    public static final int VERTICAL_BOX = 3;

    private JLabel textLabel;
    private String[] options;
    private int[] values;
    private int numOptions;
    private int selectedValue;

    private PropertyChangeSupport mPcs =
            new PropertyChangeSupport(this);

    public ButtonMenu(int width, int buttonWidth, int layout, String[] options, int[] values){
        this(width, buttonWidth, layout, options, values, -1);
    }

    public ButtonMenu(int width, int buttonWidth, int layout, String[] options, int[] values, int defaultValue){
        super();    //JPanel constructor
        //Set layout
        this.setLayout(new FlowLayout());

        numOptions = options.length;
        this.options = options;
        this.values = values;
        selectedValue = defaultValue;

        //Add the text label
        textLabel = new JLabel("");

        //Add the buttons
        JButton[] buttons = new JButton[numOptions];
        for (int i = 0; i < buttons.length; i++){
            buttons[i] = new JButton(options[i]);
            buttons[i].setActionCommand(options[i]);
            buttons[i].addActionListener(this);
            this.add(buttons[i]);
            //adjust size
        }
        //Add the button borders

    }

    public String getText(){
        return textLabel.getText();
    }

    public void setText(String newText){
        textLabel.setText(newText);
    }

    public int getSelectedValue(){
        return selectedValue;
    }

    public void actionPerformed(ActionEvent e){
        String option = e.getActionCommand();   //setActionCommand(String) for the firing object
        for (int i = 0; i < numOptions; i++){
            if (option.equals(options[i])){
                selectedValue = values[i];
                break;
            }
        }
        System.out.println(selectedValue);
        mPcs.firePropertyChange("selectedValue", -1, selectedValue);
    }


    public void
    addPropertyChangeListener(PropertyChangeListener listener) {
        mPcs.addPropertyChangeListener(listener);
    }

    public void
    removePropertyChangeListener(PropertyChangeListener listener) {
        mPcs.removePropertyChangeListener(listener);
    }
}
