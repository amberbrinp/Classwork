import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Extend this class to set some default behavior for JFrames. <br>
 */
public abstract class CenterFrame extends JFrame 
{
   public CenterFrame(int width, int height, String title)
   {  
      super (title);
      center(width, height);
      addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent event) { System.exit(0);} });
   }

   private void center(int width, int height)
   {
      setSize(width, height);

      //center the window
      Dimension screenSize = getToolkit().getScreenSize();
      int screenWidth = screenSize.width;
      int screenHeight = screenSize.height;
      setLocation(screenWidth/2 - width/2, screenHeight/2 - height/2);
   }
}


