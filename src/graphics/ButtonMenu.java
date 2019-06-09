package graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.*;

/**
 * Pre-formatted JPanel that contains a menu
 * Consists of text, then buttons with automatically created action listeners
 * Using classes can access the last selected option (button)
 *
 * @author Alexander Li
 * @version 2019-06-05 v1.0
 */
public class ButtonMenu extends JPanel implements ActionListener {
    //Class constants
    //Sizing constants
    public static final int AUTOSIZE = 1;

    //Layout constants
    public static final int FLOW_LAYOUT = 1;
    public static final int GRID_LAYOUT = 2;
    public static final int VERTICAL_BOX = 3;

    /** JLabel containing text in menu */
    private JLabel textLabel;

    /** List of option (names) of the menu */
    private String[] options;

    /** List of option values (ints) corresponding to each option */
    private int[] values;

    /** Number of options */
    private int numOptions;

    /** The last selected option, represented by its value */
    private int selectedValue;

    /** Support class to facilitate usage of a property listener with this class */
    private PropertyChangeSupport mPcs =
            new PropertyChangeSupport(this);

    /**
     * Class constructor
     * Includes default selected value parameter
     * @param width
     * @param buttonWidth
     * @param layout
     * @param options
     * @param values
     * @param defaultValue
     */
    public ButtonMenu(int width, int buttonWidth, int layout, String[] options, int[] values, int defaultValue){
        super();    //JPanel constructor
        this.setLayout(new BorderLayout());


        numOptions = options.length;
        this.options = options;
        this.values = values;
        selectedValue = defaultValue;

        //Add the text label
        textLabel = new JLabel("");
        textLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        textLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.add(textLabel, BorderLayout.NORTH);

        //Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        if (layout == FLOW_LAYOUT) buttonPanel.setLayout(new FlowLayout());
        else if (layout == GRID_LAYOUT) buttonPanel.setLayout(new GridLayout(numOptions, 1));
        else if (layout == VERTICAL_BOX) buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        //Add the buttons
        JButton[] buttons = new JButton[numOptions];
        for (int i = 0; i < buttons.length; i++){
            buttons[i] = new JButton(options[i]);
            buttons[i].setActionCommand(options[i]);
            buttons[i].addActionListener(this);
            buttons[i].setAlignmentX(JButton.CENTER_ALIGNMENT);
            buttons[i].setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            //Change button width
            Dimension d = buttons[i].getPreferredSize();
            d = new Dimension(buttonWidth, d.height);
            buttons[i].setPreferredSize(d);
            buttons[i].setMinimumSize(d);
            buttons[i].setMaximumSize(d);

            buttonPanel.add(buttons[i]);
        }

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Returns the menu's text
     * @return The text in textLabel
     */
    public String getText(){
        return textLabel.getText();
    }

    /**
     * Sets the menu's text
     * This can recognize html tags
     * @param newText The new text for textLabel
     */
    public void setText(String newText){
        textLabel.setText(newText);
    }

    /**
     * Returns the value of the last selected option
     * @return The value of the last selected option
     */
    public int getSelectedValue(){
        return selectedValue;
    }

    /**
     * Handles button presses and changes the last selected value
     * @param e The ActionEvent caught
     */
    public void actionPerformed(ActionEvent e){
        String option = e.getActionCommand();   //setActionCommand(String) for the firing object
        for (int i = 0; i < numOptions; i++){
            if (option.equals(options[i])){
                selectedValue = values[i];
                break;
            }
        }
        //Broadcast change in the selectedValue property to any attached PropertyChangeListeners
        mPcs.firePropertyChange("selectedValue", -1, selectedValue);
    }

    /**
     * Adds a PropertyChangeListener to this ButtonMenu
     * @param listener The PropertyChangeListener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        mPcs.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from this ButtonMenu
     * @param listener The PropertyChangeListener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        mPcs.removePropertyChangeListener(listener);
    }
}
