import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

class ImageLoader
{
   private static ImageLoader il = new ImageLoader();
   private Toolkit toolkit;

   //singleton design pattern hides the constructor
   private ImageLoader() 
   {
      toolkit = Toolkit.getDefaultToolkit();
   }

   //the only way to get at the singleton reference
   public static ImageLoader getImageLoader()
   {
      return il;
   }
   
   public Image getImage(String file)
   {
      int len = file.length();
      Image img = null;   

      try
      {
         BufferedImage bi = ImageIO.read(getClass().getResource(file));
         img = toolkit.createImage(bi.getSource());
      } 
      catch (IOException e){}
      
      return img;
   }

}
