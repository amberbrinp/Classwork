import java.util.ArrayList;
import java.util.Iterator;

public class BSTDriver
{
   public static void main(String[] args)
   {
      //call the height and isBalanced methods and display the results with all items inserted
       BinarySearchTree bst = new BinarySearchTree();
       System.out.println("Height: " + bst.height());
       System.out.println("Size: " + bst.size());
       System.out.println("Balanced?: " + bst.isBalanced());
       
       CD[] cds = readMusic("cds.txt");
       System.out.println("# of cds in list: " + cds.length);
       
       for (int i = 0; i < cds.length; i++)
       {
           bst.insert(cds[i]);
       }

       
       BinaryTreeIterator iter = new BinaryTreeIterator(bst.getRootNode());
       iter.setInorder();
       while (iter.hasNext())
       {
           KeyedItem ki = (KeyedItem) iter.next();
           System.out.println(ki.getKey());
       }
       
       System.out.println("Height: " + bst.height());//17
       System.out.println("Size: " + bst.size());//210
       System.out.println("Balanced?: " + bst.isBalanced());//true
       
       for (int i = 0; i < 7; i++)
       {
           bst.delete(cds[i].getKey());
       }
       System.out.println("After deleting 7: ");
       System.out.println("Height: " + bst.height());//17
       System.out.println("Size: " + bst.size());//203
       System.out.println("Balanced?: " + bst.isBalanced());//true
       
       System.out.println("Retrieved: " + bst.retrieve("Swamplord").getKey());
       try
       {
           System.out.println(bst.retrieve("Kidz Bop 42").getKey());
       }
       catch(NullPointerException npe)
       {
           System.out.println("Retrieving Kidz Bop 42: " + npe.getMessage());
       }
       
       for (int i = 0; i < cds.length; i++)
       {
           try
           {
               bst.delete(cds[i].getKey());
           }
           catch (TreeException te)
           {
               System.out.println("Result of deleting nonexistant item: " + te.getMessage());
           }
       }
       System.out.println("After deleting all: ");
       System.out.println("Height: " + bst.height());//0
       System.out.println("Size: " + bst.size());//0
       System.out.println("Balanced?: " + bst.isBalanced());//true
       //this doesn't output any assertion errors but we had quite a few of them while we were writing it
   }

   private static CD[] readMusic(String fileName)
   {
      FileIO file = new FileIO(fileName, FileIO.FOR_READING);
      String str = file.readLine();
      ArrayList<CD> cds = new ArrayList<CD>();
      while (!file.EOF())
      {
         String title = file.readLine();
         int year = Integer.parseInt(file.readLine());
         int rating = Integer.parseInt(file.readLine());
         int numTracks = Integer.parseInt(file.readLine());
         CD cd = new CD(title, str, year, rating, numTracks);

         cds.add(cd);
         int tracks = 1;

         while (tracks <= numTracks)
         {
            String temp = file.readLine();
            String[] line = temp.split(",");
            String len = line[0];
            String songTitle = line[1];
            cd.addSong(songTitle, len);
            tracks++;
         }

         str = file.readLine();
      }

      CD[] cds_array = new CD[cds.size()];
      int i = 0;
      for(CD cd : cds)
      {
         cds_array[i] = cds.get(i);
         i++;
      }
      return cds_array;
   }
}
