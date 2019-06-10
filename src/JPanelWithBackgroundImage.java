import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class JPanelWithBackgroundImage extends JPanel {

    private Image backgroundImage;

    // Some code to initialize the background image.
    // Here, we use the constructor to load the image. This
    // can vary depending on the use case of the panel.
    public JPanelWithBackgroundImage(String fileName) throws IOException, URISyntaxException {
        //backgroundImage = ImageIO.read(JPanelWithBackgroundImage.class.getResource("/images/" + fileName));
        backgroundImage = ImageIO.read(new File(FileChooser.getMediaPath(fileName)));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image.
        g.drawImage(backgroundImage, 0, 0, this);
    }
}

