package graphics;
import javax.swing.*;
import java.awt.*;

public class TestTextArea {

    public static void main(String[]Args){
        JFrame myFrame = new JFrame("TestTextAreas");
        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new FlowLayout());
        contentPane.setSize(new Dimension(400, 400));

        JLabel textArea = new JLabel();
        textArea.setText("<html><p>123456789</p><p> 123456789 123456789 123456789 123456789 123456789 123456789 123456789 </p></html>");
        textArea.setPreferredSize(new Dimension(200, 300));


        contentPane.add(textArea);
        myFrame.setContentPane(contentPane);

        myFrame.pack();
        myFrame.setVisible(true);
    }
}
