import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import javax.swing.border.*;
/**
 * Displays a picture and lets you explore the picture by displaying the row, column, red,
 * green, and blue values of the pixel at the cursor when you click a mouse button or
 * press and hold a mouse button while moving the cursor.  It also lets you zoom in or
 * out.  You can also type in a row and column value to see the color at that location.
 * 
 * Originally created for the Jython Environment for Students (JES). 
 * Modified to work with DrJava by Barbara Ericson
 * Also modified to show row and columns by Barbara Ericson
 * 
 * @author Keith McDermottt, gte047w@cc.gatech.edu
 * @author Barb Ericson ericson@cc.gatech.edu
 */
public class Level implements MouseMotionListener, ActionListener, MouseListener
{
  //Level information
  private String levelName;

  private int regions;

  private int[] regionPointsX;

  private int[] regionPointsY;

  private Color[] regionColors;


  // current indicies
  /** row index */
  private int rowIndex = 0; 
  /** column index */
  private int colIndex = 0;
  
  // main GUI
  /** window to hold GUI */
  private JFrame pictureFrame;
  /** window that allows the user to scroll to see a large picture */
  private JScrollPane scrollPane;

  //GUI ELEMENTS

  //MENU ELEMENTS
  
  /** The picture being explored */
  private Picture picture;
  
  /** The image icon used to display the picture */
  private ImageIcon scrollImageIcon;
  
  /** The image display */
  private ImageDisplay imageDisplay;

  /** the zoom factor (amount to zoom) */
  private double zoomFactor;

  //Colour palette
  private ColorPalette palette;

  private int paletteRows;
  private int paletteCols;
  private Color[] colors;
  private String[] labels;
  
  /**
   * Public constructor 
   * @param levelFolder the level directory
   */
  public Level(String levelFolder){
    String levelFile = getLevelDirectory(levelFolder) + "levelInfo.dat";
    String imageFile = null;
    try{
      BufferedReader in = new BufferedReader(new FileReader(levelFile));
      levelName = in.readLine();
      imageFile = getLevelDirectory(levelFolder) + in.readLine();
      regions = Integer.parseInt(in.readLine());
      in.readLine();

      regionPointsX = new int[regions];
      regionPointsY = new int[regions];
      regionColors = new Color[regions];

      //Read in region keypoint colours and coordinates
      for (int i = 0; i < regions; i++){
        String[] coords = in.readLine().split(",");
        String[] rgb = in.readLine().split(",");
        in.readLine();

        regionPointsX[i] = Integer.parseInt(coords[0]);
        regionPointsY[i] = Integer.parseInt(coords[1]);

        int red = Integer.parseInt(rgb[0]);
        int green = Integer.parseInt(rgb[1]);
        int blue = Integer.parseInt(rgb[2]);
        regionColors[i] = new Color(red, green, blue);
      }
      in.close();
    }
    catch (Exception e){
      System.out.println("Error in level file.");
      e.printStackTrace();
      System.exit(0);
    }

    this.picture = new Picture(imageFile);
    zoomFactor = 1;

    //Create the colour palette
    String paletteFile = getLevelDirectory(levelFolder) + "palette.dat";
    try{
      BufferedReader in = new BufferedReader(new FileReader(paletteFile));
      paletteRows = Integer.parseInt(in.readLine());
      paletteCols = Integer.parseInt(in.readLine());
      int noOfColors = Integer.parseInt(in.readLine());
      if (noOfColors > paletteRows * paletteCols) throw new Exception();

      colors = new Color[noOfColors];
      labels = new String[noOfColors];

      for (int i = 0; i < noOfColors; i++){
        String[] tokens = in.readLine().split(" ");
        int red = Integer.parseInt(tokens[0]);
        int blue = Integer.parseInt(tokens[1]);
        int green = Integer.parseInt(tokens[2]);
        colors[i] = new Color(red, blue, green);
        labels[i] = (tokens.length > 3)? tokens[3] : null;
      }
    }
    catch (Exception e){
      System.out.println("Error in palette file.");
      System.exit(0);
    }
    palette = new ColorPalette(paletteRows, paletteCols, colors, labels);

    //Create window
    createWindow();

    //Pause, for testing.
    try{
      Thread.sleep(20000);
    }catch(Exception e){}

    results();

  }

  public void results(){
    int correct = regions;
    for (int i = 0; i < regions; i++){
      Pixel p = picture.getPixel(regionPointsX[i], regionPointsY[i]);
      if (!p.getColor().equals(colors[i])) correct--;
    }

    System.out.println("You got " + correct + "/" + regions + " correct");
  }

  /**
   * Method to get the directory for the media
   * @return the media directory
   */
  public static String getLevelDirectory(String level)
  {
    String path = "../levels/" + level + "/";
    String directory = null;
    boolean done = false;
    File dirFile = null;

    // try to find the images directory
    try {
      // get the URL for where we loaded this class
      Class currClass = Class.forName("Level");
      URL classURL = currClass.getResource("Level.class");
      URL fileURL = new URL(classURL,path);
      directory = fileURL.getPath();
      directory = URLDecoder.decode(directory, "UTF-8");
      dirFile = new File(directory);
      if (dirFile.exists()) {
        //setMediaPath(directory);
        return directory;
      }
    } catch (Exception ex) {
    }

    return directory;
  }
  
  /**
   * Set the title of the frame
   *@param title the title to use in the JFrame
   */
  public void setTitle(String title)
  {
    pictureFrame.setTitle(title);
  }
  
  /**
   * Creates the JFrame and sets everything up
   */
  private void createWindow()
  {
    // create the picture frame and initialize it
    pictureFrame = new JFrame(); // create the JFrame
    pictureFrame.setResizable(false);  // allow the user to resize it
    pictureFrame.getContentPane().setLayout(new BorderLayout(10, 10)); // use border layout
    pictureFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // when close stop
    pictureFrame.setTitle(picture.getTitle());

    //Creates the GUI on top
    JPanel topRow = new JPanel();
    topRow.setLayout(new GridLayout(1, 5));
    //Quit button
    JButton quitButton = new JButton("BACK TO LEVELS");

    //Time passed label
    JPanel timeLabelPanel = new JPanel();
    timeLabelPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
    timeLabelPanel.setAlignmentY(JPanel.BOTTOM_ALIGNMENT);
    JLabel timeLabel = new JLabel("TIME PASSED: 0");
    timeLabelPanel.add(timeLabel);

    //Submit button submits the current image for evaluation
    JButton submitButton = new JButton("SUBMIT");

    //Regions left button: how many blank regions are left. (What is the blank colour?)
    JPanel regionsLabelPanel = new JPanel();
    regionsLabelPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
    regionsLabelPanel.setAlignmentY(JPanel.BOTTOM_ALIGNMENT);
    JLabel regionsLabel = new JLabel("100 REGIONS UNFILLED");
    regionsLabelPanel.add(regionsLabel);

    //Reset button. May change to general menu later on.
    //Alternatively, add a menu bar to the window
    JButton resetButton = new JButton("RESET");

    topRow.add(quitButton);
    topRow.add(timeLabelPanel);
    topRow.add(submitButton);
    topRow.add(regionsLabelPanel);
    topRow.add(resetButton);


    
    //creates the scrollpane for the picture
    scrollPane = new JScrollPane();

    BufferedImage bimg = picture.getBufferedImage();
    imageDisplay = new ImageDisplay(bimg);
    imageDisplay.addMouseMotionListener(this);
    imageDisplay.addMouseListener(this);
    imageDisplay.setToolTipText("Click on the image to fill the region with a selected colour.");
    scrollPane.setViewportView(imageDisplay);

    //Adds palette
    JPanel paletteBox = new JPanel();
    paletteBox.setLayout(new FlowLayout());
    paletteBox.setAlignmentX(JPanel.CENTER_ALIGNMENT);
    paletteBox.setAlignmentY(JPanel.CENTER_ALIGNMENT);
    paletteBox.add(palette);

    //Add to the picture frame each component
    pictureFrame.add(topRow, BorderLayout.NORTH);
    pictureFrame.add(scrollPane, BorderLayout.CENTER);
    pictureFrame.add(paletteBox, BorderLayout.SOUTH);

    // show the picture in the frame at the size it needs to be

    pictureFrame.pack();
    pictureFrame.setVisible(true);
  }
  
  /**
   * Method to check that the current position is in the viewing area and if
   * not scroll to center the current position if possible
   */
  public void checkScroll()
  {
    // get the x and y position in pixels
    int xPos = (int) (colIndex * zoomFactor); 
    int yPos = (int) (rowIndex * zoomFactor); 
    
    // only do this if the image is larger than normal
    if (zoomFactor > 1) {
      
      // get the rectangle that defines the current view
      JViewport viewport = scrollPane.getViewport();
      Rectangle rect = viewport.getViewRect();
      int rectMinX = (int) rect.getX();
      int rectWidth = (int) rect.getWidth();
      int rectMaxX = rectMinX + rectWidth - 1;
      int rectMinY = (int) rect.getY();
      int rectHeight = (int) rect.getHeight();
      int rectMaxY = rectMinY + rectHeight - 1;
      
      // get the maximum possible x and y index
      int macolIndexX = (int) (picture.getWidth() * zoomFactor) - rectWidth - 1;
      int macolIndexY = (int) (picture.getHeight() * zoomFactor) - rectHeight - 1;
      
      // calculate how to position the current position in the middle of the viewing
      // area
      int viewX = xPos - (int) (rectWidth / 2);
      int viewY = yPos - (int) (rectHeight / 2);
      
      // reposition the viewX and viewY if outside allowed values
      if (viewX < 0)
        viewX = 0;
      else if (viewX > macolIndexX)
        viewX = macolIndexX;
      if (viewY < 0)
        viewY = 0;
      else if (viewY > macolIndexY)
        viewY = macolIndexY;
      
      // move the viewport upper left point
      viewport.scrollRectToVisible(new Rectangle(viewX,viewY,rectWidth,rectHeight));
    }
  }
  
  /**
   * Repaints the image on the scrollpane.  
   */
  public void repaint()
  {
    pictureFrame.repaint();
  }
  
  //****************************************//
  //               Event Listeners          //
  //****************************************//
  
  /**
   * Called when the mouse is dragged (button held down and moved)
   * @param e the mouse event
   */
  public void mouseDragged(MouseEvent e) {}
  
  /**
   * Method to check if the given x and y are in the picture
   * @param column the horizontal value
   * @param row the vertical value
   * @return true if the row and column are in the picture 
   * and false otherwise
   */
  private boolean isLocationInPicture(int column, int row)
  {
    boolean result = false; // the default is false
    if (column >= 0 && column < picture.getWidth() &&
        row >= 0 && row < picture.getHeight())
      result = true;
    
    return result;
  }

  /**
   * Method called when the mouse is moved with no buttons down
   * @param e the mouse event
   */
  public void mouseMoved(MouseEvent e) {}
  
  /**
   * Method called when the mouse is clicked
   * @param e the mouse event
   */
  public void mouseClicked(MouseEvent e)
  {
    fill(e.getX(), e.getY());
  }
  
  /**
   * Method called when the mouse button is pushed down
   * @param e the mouse event
   */ 
  public void mousePressed(MouseEvent e){}
  
  /**
   * Method called when the mouse button is released
   * @param e the mouse event
   */
  public void mouseReleased(MouseEvent e) {}
  
  /**
   * Method called when the component is entered (mouse moves over it)
   * @param e the mouse event
   */
  public void mouseEntered(MouseEvent e) {}
  
  /**
   * Method called when the mouse moves over the component
   * @param e the mouse event
   */
  public void mouseExited(MouseEvent e) {}


  /**
   * Controls the zoom menu bar
   *
   * @param a the ActionEvent
   */
  public void actionPerformed(ActionEvent a)
  {
    if(a.getActionCommand().equals("Update"))
    {
      this.repaint();
    }
  }

  /**
   * Method to fill an enclosed region with one colour
   * @param cursorX The x position of the cursor
   * @param cursorY The y position of the cursor
   */
  private void fill(int cursorX, int cursorY)
  {
    // get the x and y in the original (not scaled image)
    int pictureX = (int) (cursorX / zoomFactor);
    int pictureY = (int) (cursorY / zoomFactor);

    Color changeColor = palette.getSelectedColor();
    // display the information for this x and y
    picture.fill(pictureX, pictureY, changeColor);
    this.repaint();
  }

  /**
   * Test Main.
   */
  public static void main( String args[])
  {
    System.out.println("Enter the folder name of the level you want to test.");
    Scanner sc = new Scanner(System.in);
    String level = sc.nextLine();
    sc.close();

    new Level(level);

  }

}
