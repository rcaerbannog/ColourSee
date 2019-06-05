package graphics;

import java.awt.*;
import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;

/**
 * Test class for various components to be added
 * Will be deleted before inclusion in the final game
 */
public class TestMenuDialog extends Frame
{
    public TestMenuDialog ()
    {
        super ("Test"); // Set the frame's name
        setSize(new Dimension(400, 400));
        setVisible(true);

        JDialog resultsDialog = new JDialog (this, "Results");

        int correct = 3;
        int regions = 4;
        String resultsText = ("You got " + correct + "/" + regions + " correct");

        //These buttons could also be used for a general level thing
        String[] options = {"Levels", "Restart Level", "Main Menu"};
        int[] values = {1, 2, 3};
        ButtonMenu menu = new ButtonMenu(200, 100, ButtonMenu.VERTICAL_BOX, options, values, 1);

        resultsDialog.setDefaultCloseOperation (JDialog.DO_NOTHING_ON_CLOSE);
        resultsDialog.getContentPane ().add (menu);



        menu.addPropertyChangeListener (
                new PropertyChangeListener()
                {
                    public void propertyChange (PropertyChangeEvent e)
                    {
                        String prop = e.getPropertyName ();

                        if (resultsDialog.isVisible ()
                                //&& (e.getSource () == resultsDialog)
                                && (prop.equals ("selectedValue")))
                        {
                            //If you were going to check something
                            //before closing the window, you'd do
                            //it here.
                            System.out.println(e.getNewValue());
                            resultsDialog.setVisible (false);
                        }
                    }
                }
        );



        resultsDialog.pack ();

        resultsDialog.setResizable (false);
        resultsDialog.setModal (true);
        //setModal pauses current execution until the JDialog is closed

        resultsDialog.setLocationRelativeTo (this);
        //addPropertyChangeListener (new ValuePropertyHandler (dialog));

        resultsDialog.setVisible (true);


    } // Constructor


    public void paint (Graphics g)
    {
        // Place the drawing code here
        g.drawLine (20, 20, 40, 40);
    } // paint method


    public static void main (String[] args)
    {
        new TestMenuDialog ();    // Create a Test frame




    } // main method
} // Test class
