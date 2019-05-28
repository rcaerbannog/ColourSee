import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.*;
import java.util.*;
import java.util.List; // resolves problem with java.awt.List and java.util.List

/**
 * A class that represents a picture.  This class inherits from 
 * Picture and allows the student to add functionality to
 * the Picture class.  
 * 
 * @author Barbara Ericson ericson@cc.gatech.edu
 */
public class Picture extends SimplePicture 
{
  ///////////////////// constructors //////////////////////////////////
  
  /**
   * Constructor that takes no arguments 
   */
  public Picture ()
  {
    /* not needed but use it to show students the implicit call to super()
     * child constructors always call a parent constructor 
     */
    super();  
  }
  
  /**
   * Constructor that takes a file name and creates the picture 
   * @param fileName the name of the file to create the picture from
   */
  public Picture(String fileName)
  {
    // let the parent class handle this fileName
    super(fileName);
  }
  
  /**
   * Constructor that takes the width and height
   * @param height the height of the desired picture
   * @param width the width of the desired picture
   */
  public Picture(int height, int width)
  {
    // let the parent class handle this width and height
    super(width,height);
  }
  
  /**
   * Constructor that takes a picture and creates a 
   * copy of that picture
   * @param copyPicture the picture to copy
   */
  public Picture(Picture copyPicture)
  {
    // let the parent class do the copy
    super(copyPicture);
  }
  
  /**
   * Constructor that takes a buffered image
   * @param image the buffered image to use
   */
  public Picture(BufferedImage image)
  {
    super(image);
  }
  
  ////////////////////// methods ///////////////////////////////////////
  
  /**
   * Method to return a string with information about this picture.
   * @return a string with information about the picture such as fileName,
   * height and width.
   */
  public String toString()
  {
    String output = "Picture, filename " + getFileName() + 
      " height " + getHeight() 
      + " width " + getWidth();
    return output;
    
  }

  /**
   * Method to open a picture explorer on a copy (in memory) of this
   * simple picture
   */
  public void explore()
  {
    // create a copy of the current picture and explore it
    new PictureExplorer(new Picture(this));
  }
  
  /** Method to set the blue to 0 */
  public void zeroBlue()
  {
    Pixel[][] pixels = this.getPixels2D();
    for (Pixel[] rowArray : pixels)
    {
      for (Pixel pixelObj : rowArray)
      {
        pixelObj.setBlue(0);
      }
    }
  }


  
  
  /** Method to show large changes in color 
    * @param edgeDist the distance for finding edges
    */
  public void edgeDetection(int edgeDist)
  {
    Pixel leftPixel = null;
    Pixel rightPixel = null;
    Pixel[][] pixels = this.getPixels2D();
    Color rightColor = null;
    for (int row = 0; row < pixels.length; row++)
    {
      for (int col = 0; 
           col < pixels[0].length-1; col++)
      {
        leftPixel = pixels[row][col];
        rightPixel = pixels[row][col+1];
        rightColor = rightPixel.getColor();
        if (leftPixel.colorDistance(rightColor) > 
            edgeDist)
          leftPixel.setColor(Color.BLACK);
        else
          leftPixel.setColor(Color.WHITE);
      }
    }
  }

  /**
   * Fills a region of a picture that is the same colour as itself
   * @param pictureX The x coordinate of the starting pixel
   * @param pictureY The y coordinate of the starting pixel
   * @param replace The Color to replace each pixel with
   */
  public void fill(int pictureX, int pictureY, Color replace){
    //Create a queue of pixels to operate on
    ArrayList<Pixel> queue = new ArrayList<Pixel>(10000);
    //Note: when accessing pixels, pixels.length is y,
    Pixel[][] pixels = this.getPixels2D();
    Pixel pixel = pixels[pictureY][pictureX];
    Color target = pixel.getColor();

    //Check if this pixel's colour is the same as the starting colour and not the same as the ending colour
    //If change Color equals target Color then there is no need to do the fill

    if (pixel.getColor().equals(replace))
      return;
    //Change the colour of this pixel
    pixel.setColor(replace);
    //Add the starting pixel to the queue
    queue.add(pixel);

    //Operate on any pixels in the queue until the queue is empty
    while (queue.size() > 0) {
      Pixel p = queue.remove(queue.size() - 1);
      p.setColor(replace);
      int pixelX = p.getX();
      int pixelY = p.getY();

      Pixel northPixel = (pixelY > 0) ? pixels[pixelY - 1][pixelX] : null;
      Pixel southPixel = (pixelY < pixels.length-1) ? pixels[pixelY + 1][pixelX] : null;
      Pixel westPixel = (pixelX > 0) ? pixels[pixelY][pixelX - 1] : null;
      Pixel eastPixel = (pixelX < pixels[0].length-1) ? pixels[pixelY][pixelX + 1] : null;


      //If pixel is the same Color as the target Color, change it to the new Color and add it to the queue
      if (northPixel != null && northPixel.getColor().equals(target))
        //northPixel.setColor(replace);
        queue.add(northPixel);
      if (eastPixel != null && eastPixel.getColor().equals(target))
        //eastPixel.setColor(replace);
        queue.add(eastPixel);
      if (westPixel != null && westPixel.getColor().equals(target))
        //westPixel.setColor(replace);
        queue.add(westPixel);
      if (southPixel != null && southPixel.getColor().equals(target))
      //southPixel.setColor(replace);
      queue.add(southPixel);

    }
  }
  
  /* Main method for testing - each class in Java can have a main 
   * method 
   */
  public static void main(String[] args) 
  {
    Picture beach = new Picture("beach.jpg");
    beach.explore();
    beach.zeroBlue();
    beach.explore();
  }
  
} // this } is the end of class Picture, put all new methods before this
