import java.text.DecimalFormat;

public class Song
{
   private String title;
   private int length;
   private static String cr = "\r\n";
   private static DecimalFormat fmt = new DecimalFormat("00");

   public Song(String title, String length)
   {
      this.title = title;

      String[] pieces = length.split(":");
      String minutes = pieces[0];
      String seconds = pieces[1];
      int min = Integer.parseInt(minutes);
      int sec = Integer.parseInt(seconds);
      this.length = 60*min + sec;
   }

   public void writeSong(FileIO file, int count)
   {
      String temp = "<td>" + fmt.format(count) + "</td><td>";
      temp += title + "</td><td>";
      temp += (length / 60) + ":" + fmt.format(length % 60) + "</td><td>";
      temp += "</td>";

      file.writeLine("               " + temp);
   }

   public String getTitle()
   {
      return title;
   }

   public int getLength()
   {
      return length;
   }

}