import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

public class ToppingTransferable implements Transferable
{
   //DataFlavor specifies what type of data is being transferred
   //flavors are necessary when drag and drop is between different JVMs (or even between native and a JVM)
   //multiple flavors allow certain flavors for the same JVM (local transfers where an object reference is returned), 
   //and other flavors between JVMs (nonlocal transfers that are tied to InputStreams rather than object references)

   //local transfer, reference to a String required
   public static DataFlavor toppingFlavor = new DataFlavor(new String().getClass(), "String");  

   //the String reference to be transferred
   private String topping;  

   public ToppingTransferable(String topping)
   {
      this.topping = topping;
   }

   //the list of possible supported flavors for this transferable 
   //used to help figure out what the object is and how to handle it when it is dropped
   public DataFlavor[] getTransferDataFlavors()
   {
      DataFlavor[] flavors = {toppingFlavor}; //only one flavor
      return flavors;
   }

   //is flavor the correct flavor for this transferable?
   public boolean isDataFlavorSupported(DataFlavor flavor)
   {
      return flavor.equals(toppingFlavor);
   }

   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
   {
      //is the data of the correct type (local transfer)?
      if (flavor.equals(toppingFlavor))
      {
         return topping;  //if it is the correct type, return a reference to it
      }
      //nonlocal transfers would require an InputStream to be returned
      else
      {
         throw new UnsupportedFlavorException(flavor);
      }
   }
}