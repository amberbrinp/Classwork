import java.util.ArrayList;
import java.util.Iterator;

public class AVLDriver
{
   public static void main(String[] args)
   {
      //call the height and isBalanced methods and display the results with all items inserted
       AVLTree Test = new AVLTree();
       
       System.out.println("Height: " + Test.height());//0
       System.out.println("size = " + Test.size());//0
       System.out.println("Is it empty before inserting: " + Test.isEmpty());//true
       System.out.println("Is it balanced before inserting: " + Test.isBalanced());//true
       
       CD[] cds = readMusic("cds.txt");
       System.out.println("CDs length = " + cds.length);
      
       for(int i = 0; i < cds.length; i++)
       {
          Test.insert(cds[i]);
       }
       
       System.out.println("Height: " + Test.height());//9
       System.out.println("size = " + Test.size());//210
       System.out.println("Is the tree empty?: " + Test.isEmpty());//false
       System.out.println("Is it balanced after inserting: " + Test.isBalanced());//true
       
       try
       {
          for(int i = 0; i < cds.length; i++)
          {
              Test.insert(cds[i]);
          }
           
          System.out.println("Height: " + Test.height());
          System.out.println("size = " + Test.size());
          System.out.println("Is the tree empty?: " + Test.isEmpty());
          System.out.println("Is it balanced after inserting: " + Test.isBalanced());
       }
       catch (TreeException e)
       {
          System.out.println(e.getMessage());//cannot add duplicate
       }
       
       for (int i = 0; i < 7; i++)
       {
           Test.delete(cds[i].getKey());
       }
       
       System.out.println("After deleting 7: ");
       System.out.println("Height: " + Test.height());//9
       System.out.println("Size: " + Test.size());//203
       System.out.println("Is the tree empty?: " + Test.isEmpty());//false
       System.out.println("Balanced?: " + Test.isBalanced());//true
       System.out.println("Retrieved: " + Test.retrieve("Swamplord").getKey());
       
       try
       {
           System.out.println(Test.retrieve("Kidz Bop 42").getKey());
       }
       catch(NullPointerException npe)
       {
           System.out.println("Retrieving Kidz Bop 42: " + npe.getMessage());//null
       }
       
       for (int i = 0; i < cds.length; i++)
       {
           try
           {
               Test.delete(cds[i].getKey());
           }
           catch (TreeException te)
           {
               System.out.println("Result of deleting nonexistant item: " + te.getMessage());//item not found x7
           }
       }
       
       System.out.println("After deleting all: ");
       System.out.println("Height: " + Test.height());//0
       System.out.println("Size: " + Test.size());//0
       System.out.println("Is the tree empty?: " + Test.isEmpty());//true
       System.out.println("Balanced?: " + Test.isBalanced());//true
      
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
