import graphics.ButtonMenu;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
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
 * Modified with fill function by Alexander Li
 * New GUI implemented by Alexander Li
 * 
 * @author Keith McDermottt, gte047w@cc.gatech.edu
 * @author Barb Ericson ericson@cc.gatech.edu
 * @author Alexander Li
 * @version 2019-06-05 v1.0
 */
public class Level implements MouseMotionListener, ActionListener, MouseListener
{
  //Level information
  private String levelName;

  private int regions;

  private int result; //Can be 0, 1, 2, or 3 in this game

  private String openDialogText;

  private String endDialogText;

  private int[] regionPointsX;

  private int[] regionPointsY;

  private Color[] regionColors;

  private Simulation cbType;

  //Game clock
  private Timer clockTimer;

  private int time;

  JLabel timeLabel;

  //Lens clock
  private Timer lensTimer;

  private int lensTime;

  JLabel lensTimeLabel;

  private boolean isLensUsable;

  private boolean isLensInfinite;

  private JButton lensButton;

  private ImageIcon lensIcon;

  private ImageIcon lensIconPressed;

  //Program flow constants
  public static final int MAIN_MENU = 3;
  public static final int RESTART = 2;
  public static final int LEVELS = 1;

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
  private Color[] appearColors;
  private String[] labels;

  int buttonPress = -1;

  /**
   * Public constructor
   */
  public Level(){
    clockTimer = new Timer(1000, this);
    clockTimer.setActionCommand("CLOCK TICK");

    time = 0;

    lensTimer = new Timer(100, this);
    lensTimer.setActionCommand("LENS CLOCK TICK");
    isLensUsable = true;
    isLensInfinite = true;
    lensTime = 0;
    //Initial delay (time to event firing) will be set with file output
  }

  /*
    FILE INFO
    LEVELDATA
    level name
    number of regions
    <line>
    beginning dialog text
    <EOT>
    Point
    Correct Color (RGB)
    <line>
    <EOP>
    end dialog text
    <EOF>


   */

  /**
   * Runs a level of the game
   * Reads level data from the selected level directory, including image, palette, and key points
   * @param levelFolder the level directory
   * @param cbType the type of colorblindness to simulate
   * @return The action ID to execute once the level ends/is terminated
   */
  public int runGame(String levelFolder, Simulation cbType){
    String levelFile = FileChooser.getLevelDirectory(levelFolder) + "levelInfo.dat";
    String imageFile = null;
    this.cbType = cbType;

    try{
      BufferedReader in = new BufferedReader(new FileReader(levelFile));
      String inputLine = "";

      levelName = in.readLine();
      imageFile = FileChooser.getLevelDirectory(levelFolder) + "levelIMG.png";
      regions = Integer.parseInt(in.readLine());

      //Set up lens time constraints
      //rawLensTime is the lens time stored in levelInfo.dat in seconds
      int rawLensTime = Integer.parseInt(in.readLine());
      if (rawLensTime == 0){
        isLensUsable = false;
        isLensInfinite = false;
        lensTime = 0;
      }
      else if (rawLensTime < 0){
        isLensUsable = true;
        isLensInfinite = false;
        lensTime = Integer.MAX_VALUE;
      }
      else{
        isLensUsable = true;
        isLensInfinite = false;
        lensTime = rawLensTime*10;
      }

      in.readLine();

      //Get open dialog text
      openDialogText = "<html>";
      inputLine = in.readLine();
      while (!inputLine.equals("<EOT>")){
        openDialogText += "<p>";
        openDialogText += inputLine;
        openDialogText += "</p>";
        inputLine = in.readLine();
      }
      openDialogText += "</html>";

      //Get end dialog text
      endDialogText = "<html>";
      inputLine = in.readLine();
      while (!inputLine.equals("<EOT>")){
        endDialogText += "<p>";
        endDialogText += inputLine;
        endDialogText += "</p>";
        inputLine = in.readLine();
      }
      openDialogText += "</html>";
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

    //make image
    this.picture = new Picture(imageFile);
    zoomFactor = 1;


    //Create the colour palette
    String paletteFile = FileChooser.getLevelDirectory(levelFolder) + "palette.dat";
    try{
      BufferedReader in = new BufferedReader(new FileReader(paletteFile));
      paletteRows = Integer.parseInt(in.readLine());
      paletteCols = Integer.parseInt(in.readLine());
      int noOfColors = Integer.parseInt(in.readLine());
      if (noOfColors > paletteRows * paletteCols) throw new Exception();

      colors = new Color[noOfColors];
      labels = new String[noOfColors];
      appearColors = new Color[noOfColors];

      Simulator sim = new Simulator();
      sim.simulate(cbType);
      for (int i = 0; i < noOfColors; i++){
        String[] tokens = in.readLine().split(" ");
        int red = Integer.parseInt(tokens[0]);
        int blue = Integer.parseInt(tokens[1]);
        int green = Integer.parseInt(tokens[2]);
        colors[i] = new Color(red, blue, green);
        appearColors[i] = sim.filterColor(colors[i]);
        labels[i] = (tokens.length > 3)? tokens[3] : null;
      }
    }
    catch (Exception e){
      System.out.println("Error in palette file.");
      System.exit(0);
    }


    palette = new ColorPalette(paletteRows, paletteCols, colors, appearColors, labels);

    //Create window
    createWindow();
    addFilter();

    if (openDialogText != null)
    JOptionPane.showMessageDialog(pictureFrame, openDialogText);
    clockTimer.start();

    if (!isLensUsable){
      lensButton.setEnabled(false);
    }

    try{
      //While no action has been taken to end the level
      while (buttonPress == -1){
        Thread.sleep(10);
      }
      clockTimer.stop();
      lensTimer.stop();
      imageDisplay.setImage(picture.getBufferedImage());
      this.repaint();
      //When the user presses a top row GUI Button
      if (buttonPress == 0){
        pictureFrame.dispose();
        return LEVELS;
      }
      else if (buttonPress == 1){
        int action = results();
        pictureFrame.dispose();
        return action;
      }
      else if (buttonPress == 2){
        pictureFrame.dispose();
        return RESTART;
      }
    }catch(Exception e){return -1;}

    pictureFrame.dispose();
    return 0;
  }

  /**
   * Evaluates the player's results for this level and displays the end-of-level menu
   * Compares the Color of each keypoint pixel to the Color stored in regionColors to see if they are equal
   * Displays results, end-of-level message, and menu with option to restart, go to level selection, or main menu
   * @return The action ID to execute once the level ends/is terminated
   */
  public int results(){
    removeFilter();
    lensTimer.stop();

    int correct = regions;
    for (int i = 0; i < regions; i++){
      Pixel p = picture.getPixel(regionPointsX[i], regionPointsY[i]);
      if (!p.getColor().equals(colors[i])) correct--;
    }

    //Create dialog with options
    //RESTART
    //LEVELS
    //MAIN MENU
    JDialog resultsDialog = new JDialog (pictureFrame, "Results");

    String resultsText = ("You got " + correct + "/" + regions + " correct");

    //These buttons could also be used for a general level thing
    String[] options = {"Levels", "Restart Level", "Main Menu"};
    int[] values = {LEVELS, RESTART, MAIN_MENU};
    ButtonMenu menu = new ButtonMenu(200, 100, ButtonMenu.VERTICAL_BOX, options, values, 1);

    if (endDialogText == null){
      String menuText = "<html>" + resultsText + "</html>";
      menu.setText(menuText);
    }
    else{
      //Note: "<HTML>" is the first six characters. Cut off the html tag starting from index 6.
      String menuText = "<html>" + resultsText;
      menuText += endDialogText.substring(6); //endDialogText has a <html> tag (cut off) and an </html> tag included
      menu.setText(menuText);
    }
    resultsDialog.setDefaultCloseOperation (JDialog.DO_NOTHING_ON_CLOSE);
    resultsDialog.getContentPane ().add (menu);

    menu.addPropertyChangeListener (
            new PropertyChangeListener()
            {
              public void propertyChange (PropertyChangeEvent e)
              {
                String prop = e.getPropertyName ();
                if (resultsDialog.isVisible ()
                        && (e.getSource () == menu)
                        && (prop.equals ("selectedValue")))
                {
                  resultsDialog.setVisible (false);
                }
              }
            }
    );

    resultsDialog.pack ();
    resultsDialog.setResizable (false);
    resultsDialog.setModal (true);  //setModal pauses current execution until the JDialog is closed
    resultsDialog.setLocationRelativeTo (pictureFrame);
    resultsDialog.setVisible (true);

    //Get the option that is chosen in the dialog and return it
    return menu.getSelectedValue();
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
    pictureFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when close stop the program
    pictureFrame.setTitle(picture.getTitle());

    //Creates the GUI on top
    JPanel topRow = new JPanel();
    topRow.setLayout(new GridLayout(1, 5));
    //Quit button
    JButton quitButton = new JButton("BACK TO LEVELS");
    quitButton.setActionCommand("BACK TO LEVELS");
    quitButton.addActionListener(this);

    //Time passed label
    JPanel timeLabelPanel = new JPanel();
    timeLabelPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
    timeLabelPanel.setAlignmentY(JPanel.BOTTOM_ALIGNMENT);
    timeLabel = new JLabel("TIME PASSED: 0");
    timeLabelPanel.add(timeLabel);

    //Submit button submits the current image for evaluation
    JButton submitButton = new JButton("SUBMIT");
    submitButton.setActionCommand("SUBMIT");
    submitButton.addActionListener(this);

    //Regions left button: how many blank regions are left. (What is the blank colour?)
    JPanel regionsLabelPanel = new JPanel();
    regionsLabelPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
    regionsLabelPanel.setAlignmentY(JPanel.BOTTOM_ALIGNMENT);
    JLabel regionsLabel = new JLabel("100 REGIONS UNFILLED");
    regionsLabelPanel.add(regionsLabel);

    //Reset button. May change to general menu later on.
    //Alternatively, add a menu bar to the window
    JButton resetButton = new JButton("RESET");
    resetButton.setActionCommand("RESET");
    resetButton.addActionListener(this);

    topRow.add(quitButton);
    topRow.add(timeLabelPanel);
    topRow.add(submitButton);
    topRow.add(regionsLabelPanel);
    topRow.add(resetButton);
    
    //creates the scrollpane for the picture
    scrollPane = new JScrollPane();

    BufferedImage filteredIMG = picture.getBufferedImage();
    imageDisplay = new ImageDisplay(filteredIMG);
    imageDisplay.addMouseMotionListener(this);
    imageDisplay.addMouseListener(this);
    imageDisplay.setToolTipText("Click on the image to fill the region with a selected colour.");
    scrollPane.setViewportView(imageDisplay);

    //Add lens and lens timer to palette
    JPanel lensBox = new JPanel();
    lensBox.setLayout(new BorderLayout());
    lensBox.setAlignmentY(JPanel.CENTER_ALIGNMENT);
    lensButton = new JButton("");
    Dimension lensD = new Dimension(80, 80);
    lensButton.setPreferredSize(lensD);
    lensButton.setMinimumSize(lensD);
    lensButton.setMaximumSize(lensD);
    lensButton.setActionCommand("LENS BUTTON PRESS");
    lensButton.addActionListener(this);
    try{
      Image lensImage = ImageIO.read(new File(FileChooser.getMediaPath("magGlassIcon80.png")));
      Image lensImagePressed = ImageIO.read(new File(FileChooser.getMediaPath("magGlassIcon80_PRESSED.png")));
      lensIcon = new ImageIcon(lensImage);
      lensIconPressed = new ImageIcon(lensImagePressed);
      lensButton.setIcon(lensIcon);
    }catch(Exception e){
      System.out.println("Lens button icon not found");
      lensButton.setText("Lens");
    }
    lensTimeLabel = new JLabel(Integer.toString(lensTime/10));
    lensTimeLabel.setHorizontalAlignment(JLabel.CENTER);
    if (!isLensUsable) lensTimeLabel.setText("Lens Disabled");
    else if (isLensInfinite) lensTimeLabel.setText("Infinite Lens");
    lensBox.add(lensButton, BorderLayout.CENTER);
    lensBox.add(lensTimeLabel, BorderLayout.SOUTH);
    //Add the palette box to the palette
    palette.add(lensBox, 0);
    palette.add(Box.createRigidArea(new Dimension(20, 0)), 1);
    palette.revalidate();

    //Adds palette to JPanel
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
   * Calls the fill method on the clicked pixel
   * @param e the mouse event
   */
  public void mouseClicked(MouseEvent e)
  {
    fill(e.getX(), e.getY());
    if (!lensTimer.isRunning())
      imageDisplay.setImage(createFilteredImage(picture.getBufferedImage(), cbType));
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
   * Controls the GUI actions
   *
   * @param a the ActionEvent
   */
  public void actionPerformed(ActionEvent a)
  {
    String action = a.getActionCommand();
    if(action.equals("Update"))
    {
      this.repaint();
    }
    else if (action.equals("CLOCK TICK")){
      time++;
      timeLabel.setText("TIME PASSED: " + time);
      this.repaint();
    }
    else if (action.equals("LENS CLOCK TICK")){
      lensTime--;
      if (lensTime == 0){
        lensTimer.stop();
        isLensUsable = false;
        lensButton.setEnabled(false);
        imageDisplay.setImage(createFilteredImage(picture.getBufferedImage(), cbType));
        this.repaint();
      }
      lensTimeLabel.setText(Integer.toString(lensTime/10));
      lensTimeLabel.repaint();
    }
    else if (action.equals("LENS BUTTON PRESS")){
      if (lensTimer.isRunning())
        addFilter();
      else
        removeFilter();
    }
    else if (action.equals("REMOVE FILTER")){
      removeFilter();
    }
    else if (action.equals("ADD FILTER")){
      addFilter();
    }
    //Exit commands
    else if (action.equals("BACK TO LEVELS")) buttonPress = 0;
    else if (action.equals("SUBMIT")) buttonPress = 1;
    else if (action.equals("RESET")) buttonPress = 2;
  }

  public void removeFilter(){
    //Change image displayed to picture
    //Change button colours to true color
    imageDisplay.setImage(picture.getBufferedImage());
    lensButton.setIcon(lensIconPressed);
    palette.changeButtonsToButtonColor();
    lensTimer.start();
    this.repaint();
  }

  public void addFilter(){
    //Change image displayed to filteredPic
    //Change button colours to filtered color
    lensTimer.stop();
    lensButton.setIcon(lensIcon);
    imageDisplay.setImage(createFilteredImage(picture.getBufferedImage(), cbType));
    palette.changeButtonsToAppearColor();
    this.repaint();
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
   * Creates a colorblind filtered version of an image
   * @param normal The normal image
   * @return The filtered image
   */
  public BufferedImage createFilteredImage(BufferedImage normal, Simulation filterType){
    Simulator sim = new Simulator();
    sim.simulate(filterType);
    BufferedImage normalBF = normal;
    BufferedImage filteredBF = new BufferedImage(normalBF.getWidth(), normalBF.getHeight(), BufferedImage.TYPE_INT_RGB);
    //This method of copying the BufferedImage avoids conflicts in BufferedImage type
    //Incompatible type causes an error in the Simulator class
    filteredBF.getGraphics().drawImage(normalBF, 0, 0, null);
    filteredBF = sim.filter(filteredBF);
    return filteredBF;
  }

  public void setLensButtonActive(boolean flag){
    if (flag)
      lensButton.setEnabled(true);
    else
      lensButton.setEnabled(false);
  }
}
